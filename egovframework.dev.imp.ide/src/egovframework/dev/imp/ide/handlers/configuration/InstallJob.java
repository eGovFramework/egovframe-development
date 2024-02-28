package egovframework.dev.imp.ide.handlers.configuration;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.equinox.p2.core.ProvisionException;
import org.eclipse.equinox.p2.engine.IProfile;
import org.eclipse.equinox.p2.engine.IProfileRegistry;
import org.eclipse.equinox.p2.metadata.IInstallableUnit;
import org.eclipse.equinox.p2.operations.InstallOperation;
import org.eclipse.equinox.p2.operations.ProvisioningSession;
import org.eclipse.equinox.p2.query.IQuery;
import org.eclipse.equinox.p2.query.IQueryResult;
import org.eclipse.equinox.p2.query.QueryUtil;
import org.eclipse.equinox.p2.repository.IRepository;
import org.eclipse.equinox.p2.repository.artifact.IArtifactRepositoryManager;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepository;
import org.eclipse.equinox.p2.repository.metadata.IMetadataRepositoryManager;
import org.eclipse.equinox.p2.ui.LoadMetadataRepositoryJob;
import org.eclipse.equinox.p2.ui.ProvisioningUI;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Display;

public class InstallJob implements IRunnableWithProgress {

	private final ProvisioningUI provisioningUI;

	public InstallJob() {
		provisioningUI = ProvisioningUI.getDefaultUI();
	}

	public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
		try {
			queryRepository();
		} catch (MalformedURLException e) {
			throw new InvocationTargetException(e);
		} catch (ProvisionException e) {
			throw new InvocationTargetException(e);
		} catch (OperationCanceledException e) {
			throw new InvocationTargetException(e);
		} catch (URISyntaxException e) {
			throw new InvocationTargetException(e);
		} catch (IOException e) {
			throw new InvocationTargetException(e);
		}
	}

	public Set<IInstallableUnit> pathRepositoy(IMetadataRepositoryManager meta, IArtifactRepositoryManager artifact,
			URI uri, String nick, String extraQuery) throws ProvisionException {
		IMetadataRepository repository = null;

		if (meta.contains(uri)) {
			repository = meta.loadRepository(uri, null);
		} else {
			meta.addRepository(uri);
			repository = meta.createRepository(uri, nick, IMetadataRepositoryManager.TYPE_COMPOSITE_REPOSITORY, null);

			meta.setRepositoryProperty(uri, IRepository.PROP_NICKNAME, nick);
			artifact.addRepository(uri);
			artifact.setRepositoryProperty(uri, IRepository.PROP_NICKNAME, nick);
		}

		IQuery<IInstallableUnit> query = extraQuery == null ? QueryUtil.createIUGroupQuery()
				: QueryUtil.createIUQuery(extraQuery);
		IQueryResult<IInstallableUnit> result = repository.query(QueryUtil.createLatestQuery(query), null);

		return result.toSet();
	}

	public List<IInstallableUnit> filterInstalled(IProfile[] profiles, Set<IInstallableUnit> set) {
		List<IInstallableUnit> result = new ArrayList<IInstallableUnit>();
		Iterator<IInstallableUnit> i = set.iterator();
		while (i.hasNext()) {
			IInstallableUnit unit = i.next();
			if (!isInstalled(profiles, unit)) {
				result.add(unit);
			}
		}
		return result;
	}

	public boolean isInstalled(IProfile[] profiles, IInstallableUnit unit) {

		for (int idx = 0; idx < profiles.length; idx++) {
			IQueryResult<IInstallableUnit> queryResult = profiles[idx].query(QueryUtil.createIUQuery(unit.getId()),
					null);
			if (queryResult.isEmpty())
				continue;

			Iterator<IInstallableUnit> i = queryResult.iterator();
			while (i.hasNext()) {
				IInstallableUnit installed = (IInstallableUnit) i.next();
				int compute = installed.getVersion().compareTo(unit.getVersion());

				if (compute == 0) {
//                    System.out.println(unit.getId() + " is installed : " + unit.getVersion());
					return true;
				} else if (compute > 0) {
//                    System.out.println(unit.getId() + " is installed newer version (" + installed.getVersion() + ") vs. update site(" + unit.getVersion() + ")");
					return true;
//                } else {
//                    System.out.println(unit.getId() + " needs update : " + installed.getVersion() + " -> " + unit.getVersion());
				}
			}
		}

		return false;

	}

	private void queryRepository()
			throws URISyntaxException, ProvisionException, OperationCanceledException, IOException {
		ProvisioningSession session = provisioningUI.getSession();

		// 세로운 업데이트 사이트 운영으로 변경 (2017.07.04-sh.jang)
		final URI[] uris = { new URL("https://maven.egovframe.go.kr/update_4.2").toURI(),
				/* new URL("http://update.eclemma.org/").toURI(), */ new URL(
						"https://download.springsource.com/release/TOOLS/sts4/update/4.17.2.RELEASE/e4.26/").toURI() };
		// final URI[] uris = { new URL("http://www.egovframe.go.kr/update").toURI(),
		// /*new URL("http://update.eclemma.org/").toURI(),*/ new
		// URL("http://dist.springframework.org/release/IDE").toURI() };
//      final URI[] uris = { new URL("http://192.168.100.209:8080/update").toURI(), /*new URL("http://update.eclemma.org/").toURI(),*/ new URL("http://dist.springframework.org/release/IDE").toURI() };

		IMetadataRepositoryManager meta = (IMetadataRepositoryManager) session.getProvisioningAgent()
				.getService(IMetadataRepositoryManager.SERVICE_NAME);
		IArtifactRepositoryManager artifact = (IArtifactRepositoryManager) session.getProvisioningAgent()
				.getService(IArtifactRepositoryManager.SERVICE_NAME);
		IProfileRegistry profileRegistry = (IProfileRegistry) session.getProvisioningAgent()
				.getService(IProfileRegistry.SERVICE_NAME);
		IProfile[] profiles = profileRegistry.getProfiles();

		List<IInstallableUnit> egov = filterInstalled(profiles,
				pathRepositoy(meta, artifact, uris[0], "egovFrame", null)); // URL("http://www.egovframe.go.kr/update");
//      List<IInstallableUnit> emma = filterInstalled(profiles, pathRepositoy(meta, artifact, uris[1], "EclEmma", "com.mountainminds.eclemma.feature.feature.group"));
		List<IInstallableUnit> spring = filterInstalled(profiles, pathRepositoy(meta, artifact, uris[1], "Spring IDE",
				"org.springframework.ide.eclipse.webflow.feature.feature.group"));

		final List<IInstallableUnit> installUnits = new ArrayList<IInstallableUnit>();
		installUnits.addAll(egov);
//        installUnits.addAll(emma);
		installUnits.addAll(spring);

		Display.getDefault().asyncExec(new Runnable() {

			public void run() {
				InstallOperation op = provisioningUI.getInstallOperation(installUnits, uris);
				provisioningUI.openInstallWizard(installUnits, op, new LoadMetadataRepositoryJob(provisioningUI));

			}
		});
	}

}
