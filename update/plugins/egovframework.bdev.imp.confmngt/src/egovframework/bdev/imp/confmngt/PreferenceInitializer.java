package egovframework.bdev.imp.confmngt;

import static egovframework.bdev.imp.confmngt.preferences.readwrite.JobRWPreferencePage.JOB_READER_PREFERENCE_STORE_NAME;
import static egovframework.bdev.imp.confmngt.preferences.readwrite.JobRWPreferencePage.JOB_WRITER_PREFERENCE_STORE_NAME;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import egovframework.bdev.imp.confmngt.preferences.readwrite.model.DefaultJobRW;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.DefaultJobRWMap;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobRWInfo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobReaderInfo;
import egovframework.bdev.imp.confmngt.preferences.readwrite.model.JobWriterInfo;
import egovframework.dev.imp.core.utils.NullUtil;
import egovframework.dev.imp.core.utils.PrefrencePropertyUtil;

/**
 * Preference Store에 기본 Job Reader / Writer를 설정
 * 
 * @since 2012.10.24
 * @author 배치개발환경 개발팀 조용현
 * @version 1.0
 * @see <pre>
 *  &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *    
 * 수정일	  	수정자	  수정내용
 * -----------	------	----------------
 * 2012.10.24	조용현	최초생성
 * 
 * 
 * </pre>
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
	/** 추가할 기본 Job Reader / Writer의 List */
	private DefaultJobRWList defaultJobRWList = null;
	
	/** 기본 Job Reader / Writer가 추가된 상태인지를 확인하는 PreferenceStore의 Key값 */
	final private String IS_BATCH_JOB_RW_DEFAULT_SET = "IsBatchJobRWDefaultSet";

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = EgovBatchConfMngtPlugin.getDefault()
				.getPreferenceStore();

		boolean isBatchJobRWDefaultSet = store
				.getBoolean(IS_BATCH_JOB_RW_DEFAULT_SET);

		if (!isBatchJobRWDefaultSet) {
			defaultJobRWList = new DefaultJobRWList();

			setDefaultJobReaderList();
			addDefaultJobReaderToStore(store);

			setDefaultJobWriterList();
			addDefaultJobWriterToStore(store);

			store.setValue(IS_BATCH_JOB_RW_DEFAULT_SET, true);
		}
	}

	/** 기본 Job Reader List를 설정한다. */
	private void setDefaultJobReaderList() {
		addDefaultJobReaderToList();

		defaultJobRWList
				.removeAllPreExistJobReader(getStoredJobReaderInfoList());
	}

	/** 기본 Job Reader들을 List에 추가한다. */
	private void addDefaultJobReaderToList() {
		defaultJobRWList.makeAndAddDefaultJobReader("FixedFlatFileItemReader",
				DefaultJobRW.FIXED_FLAT_FILE_ITEM_READER);

		defaultJobRWList.makeAndAddDefaultJobReader(
				"DelimitedFlatFileItemReader",
				DefaultJobRW.DELIMITED_FLAT_FILE_ITEM_READER);

		defaultJobRWList.makeAndAddDefaultJobReader(
				"EgovFixedFlatFileItemReader",
				DefaultJobRW.EGOV_FIXED_FLAT_FILE_ITEM_READER);

		defaultJobRWList.makeAndAddDefaultJobReader(
				"EgovDelimitedFlatFileItemReader",
				DefaultJobRW.EGOV_DELIMITED_FLAT_FILE_ITEM_READER);

		defaultJobRWList.makeAndAddDefaultJobReader(
				"FixedMultiResourceItemReader",
				DefaultJobRW.FIXED_MULTI_RESOURCE_ITEM_READER);

		defaultJobRWList.makeAndAddDefaultJobReader(
				"DelimitedMultiResourceItemReader",
				DefaultJobRW.DELIMITED_MULTI_RESOURCE_ITEM_READER);

		defaultJobRWList.makeAndAddDefaultJobReader(
				"EgovFixedMultiResourceItemReader",
				DefaultJobRW.EGOV_FIXED_MULTI_RESOURCE_ITEM_READER);

		defaultJobRWList.makeAndAddDefaultJobReader(
				"EgovDelimitedMultiResourceItemReader",
				DefaultJobRW.EGOV_DELIMITED_MULTI_RESOURCE_ITEM_READER);

		defaultJobRWList.makeAndAddDefaultJobReader("IbatisPagingItemReader",
				DefaultJobRW.IBATIS_PAGING_ITEM_READER);

		defaultJobRWList.makeAndAddDefaultJobReader(
				"CustomizedJdbcCursorItemReader",
				DefaultJobRW.CUSTOMIZED_JDBC_CURSOR_ITEM_READER);

		defaultJobRWList.makeAndAddDefaultJobReader(
				"SqlPagingQueryJdbcItemReader",
				DefaultJobRW.SQL_PAGING_QUERY_JDBC_ITEM_READER);
	}
	
	/**
	 * 기존 PreferenceStore에 저장되어 있는 Job Reader의 List를 가져온다.
	 * 
	 * @return
	 */
	private ArrayList<JobReaderInfo> getStoredJobReaderInfoList() {
		ArrayList<JobReaderInfo> infoList = new ArrayList<JobReaderInfo>();
		int voCnt = EgovBatchConfMngtPlugin.getDefault().getPreferenceStore()
				.getInt(JOB_READER_PREFERENCE_STORE_NAME);

		for (int i = 0; i < voCnt; i++) {
			JobReaderInfo storedJobReaderInfo = new JobReaderInfo();
			storedJobReaderInfo.setId(JOB_READER_PREFERENCE_STORE_NAME, i);

			storedJobReaderInfo = (JobReaderInfo) PrefrencePropertyUtil
					.loadPreferences(EgovBatchConfMngtPlugin.getDefault(),
							storedJobReaderInfo);
			infoList.add(storedJobReaderInfo);
		}

		return infoList;
	}

	/** 기본 Job Writer List를 설정한다. */
	private void setDefaultJobWriterList() {
		addDefaultJobWriterToList();

		defaultJobRWList
				.removeAllPreExistJobWriter(getStoredJobWriterInfoList());
	}

	/** 기본 Job Writer들을 List에 추가한다. */
	private void addDefaultJobWriterToList() {
		defaultJobRWList.makeAndAddDefaultJobWriter(
				"DelimitedFlatFileItemWriter",
				DefaultJobRW.DELIMITED_FLAT_FILE_ITEM_WRITER);

		defaultJobRWList.makeAndAddDefaultJobWriter(
				"FormatterFlatFileItemWriter",
				DefaultJobRW.FORMATTER_FLAT_FILE_ITEM_WRITER);

		defaultJobRWList.makeAndAddDefaultJobWriter(
				"EgovDelimitedFlatFileItemWriter",
				DefaultJobRW.EGOV_DELIMITED_FLAT_FILE_ITEM_WRITER);

		defaultJobRWList.makeAndAddDefaultJobWriter(
				"EgovFixedFlatFileItemWriter",
				DefaultJobRW.EGOV_FIXED_FLAT_FILE_ITEM_WRITER);

		defaultJobRWList.makeAndAddDefaultJobWriter(
				"DelimitedMultiResourceItemWriter",
				DefaultJobRW.DELIMITED_MULTI_RESOURCE_ITEM_WRITER);

		defaultJobRWList.makeAndAddDefaultJobWriter(
				"FormatterMultiResourceItemWriter",
				DefaultJobRW.FORMATTER_MULTI_RESOURCE_ITEM_WRITER);

		defaultJobRWList.makeAndAddDefaultJobWriter(
				"EgovDelimitedMultiResourceItemWriter",
				DefaultJobRW.EGOV_DELIMITED_MULTI_RESOURCE_ITEM_WRITER);

		defaultJobRWList.makeAndAddDefaultJobWriter(
				"EgovFixedMultiResourceItemWriter",
				DefaultJobRW.EGOV_FIXED_MULTI_RESOURCE_ITEM_WRITER);

		defaultJobRWList.makeAndAddDefaultJobWriter("IbatisBatchItemWriter",
				DefaultJobRW.IBATIS_BATCH_ITEM_WRITER);

		defaultJobRWList.makeAndAddDefaultJobWriter(
				"SqlParameterJdbcBatchItemWriter",
				DefaultJobRW.SQL_PARAMETER_JDBC_BATCH_ITEM_WRITER);

		defaultJobRWList.makeAndAddDefaultJobWriter(
				"ItemPreparedStatementJdbcBatchItemWriter",
				DefaultJobRW.ITEM_PREPARED_STATEMENT_JDBC_BATCH_ITEM_WRITER);

		defaultJobRWList
				.makeAndAddDefaultJobWriter(
						"EgovItemPreparedStatementJdbcBatchItemWriter",
						DefaultJobRW.EGOV_ITEM_PREPARED_STATEMENT_JDBC_BATCH_ITEM_WRITER);

		defaultJobRWList.makeAndAddDefaultJobWriter(
				"EgovCustomizedJdbcBatchItemWriter",
				DefaultJobRW.EGOV_CUSTOMIZED_JDBC_BATCH_ITEM_WRITER);
	}

	/**
	 * 기존 PreferenceStore에 저장되어 있는 Job Writer List를 가져온다.
	 * 
	 * @return
	 */
	private ArrayList<JobWriterInfo> getStoredJobWriterInfoList() {
		ArrayList<JobWriterInfo> infoList = new ArrayList<JobWriterInfo>();
		int voCnt = EgovBatchConfMngtPlugin.getDefault().getPreferenceStore()
				.getInt(JOB_WRITER_PREFERENCE_STORE_NAME);

		for (int i = 0; i < voCnt; i++) {
			JobWriterInfo storedJobWriterInfo = new JobWriterInfo();
			storedJobWriterInfo.setId(JOB_WRITER_PREFERENCE_STORE_NAME, i);

			storedJobWriterInfo = (JobWriterInfo) PrefrencePropertyUtil
					.loadPreferences(EgovBatchConfMngtPlugin.getDefault(),
							storedJobWriterInfo);
			infoList.add(storedJobWriterInfo);
		}

		return infoList;
	}

	/**
	 * 설정된 기본 Job Reader List를 PreferenceStore에 추가한다.
	 * 
	 * @param store
	 */
	private void addDefaultJobReaderToStore(IPreferenceStore store) {
		int currentVoCount = store.getInt(JOB_READER_PREFERENCE_STORE_NAME);

		List<JobReaderInfo> defaultJobReaderList = defaultJobRWList
				.getDefaultJobReaderList();

		for (JobReaderInfo defaultJobRWInfo : defaultJobReaderList) {
			defaultJobRWInfo.setId(JOB_READER_PREFERENCE_STORE_NAME,
					currentVoCount);
			currentVoCount++;

			PrefrencePropertyUtil.savePreferences(
					EgovBatchConfMngtPlugin.getDefault(), defaultJobRWInfo);
		}

		store.setValue(JOB_READER_PREFERENCE_STORE_NAME, currentVoCount);
	}

	/**
	 * 설정된 기본 Job Writer List를 PreferenceStore에 추가한다.
	 * 
	 * @param store
	 */
	private void addDefaultJobWriterToStore(IPreferenceStore store) {
		int currentVoCount = store.getInt(JOB_WRITER_PREFERENCE_STORE_NAME);

		List<JobWriterInfo> defaultJobWriterList = defaultJobRWList
				.getDefaultJobWriterList();

		for (JobWriterInfo defaultJobRWInfo : defaultJobWriterList) {
			defaultJobRWInfo.setId(JOB_WRITER_PREFERENCE_STORE_NAME,
					currentVoCount);
			currentVoCount++;

			PrefrencePropertyUtil.savePreferences(
					EgovBatchConfMngtPlugin.getDefault(), defaultJobRWInfo);
		}

		store.setValue(JOB_WRITER_PREFERENCE_STORE_NAME, currentVoCount);
	}

}

/** 기본 Job Reader / Writer의 List를 가지고 있는 내부 Class VO */
class DefaultJobRWList {
	/** PreferenceStore에 추가할 기본 Job Reader 리스트 */
	private ArrayList<JobReaderInfo> defaultJobReaderList = null;
	
	/** PreferenceStore에 추가할 기본 Job Writer 리스트  */
	private ArrayList<JobWriterInfo> defaultJobWriterList = null;

	/** 기본 Job Reader의 Type, Class를 담는 Map */
	private DefaultJobRWMap defaultJobReaderMap = null;
	
	/** 기본 Job Writer의 Type, Class를 담는 Map */
	private DefaultJobRWMap defaultJobWriterMap = null;

	/** DefaultJobRWList의 생성자 */
	public DefaultJobRWList() {
		defaultJobReaderList = new ArrayList<JobReaderInfo>();
		defaultJobWriterList = new ArrayList<JobWriterInfo>();

		defaultJobReaderMap = new DefaultJobRWMap();
		defaultJobReaderMap.resetDefaultReadType();

		defaultJobWriterMap = new DefaultJobRWMap();
		defaultJobWriterMap.resetDefaultWriterType();
	}

	/**
	 * 넘어온 name, ReaderType(ItemType)을 이용해 Job Reader를 생성하고 List에 추가한다.
	 * 
	 * @param name
	 * @param itemType
	 */
	public void makeAndAddDefaultJobReader(String name, String itemType) {
		JobReaderInfo defaultJobReaderInfo = makeDefaultJobReader(name,
				itemType);

		defaultJobReaderList.add(defaultJobReaderInfo);
	}

	/**
	 * 넘어온 name, ReaderType(ItemType)을 이용해 Job Reader를 생성
	 * 
	 * @param name
	 * @param itemType
	 * @return
	 */
	private JobReaderInfo makeDefaultJobReader(final String name,
			final String itemType) {
		JobReaderInfo jobReaderInfo = new JobReaderInfo();

		jobReaderInfo.setName(name);

		if (defaultJobReaderMap.isFileType(itemType)) {
			jobReaderInfo.setResourceType(JobRWInfo.FILE);
		} else {
			jobReaderInfo.setResourceType(JobRWInfo.DB);
		}

		jobReaderInfo.setItemType(itemType);

		String classValue = defaultJobReaderMap.getClassValue(itemType);
		jobReaderInfo.setClassValue(classValue);

		String resourceDetailType = defaultJobReaderMap.getDetailType(itemType);
		jobReaderInfo.setResourceDetailType(resourceDetailType);

		return jobReaderInfo;
	}

	/**
	 * 넘어온 name, WriterType(ItemType)을 이용해 Job Writer를 생성하고 List에 추가한다.
	 * 
	 * @param name
	 * @param itemType
	 */
	public void makeAndAddDefaultJobWriter(String name, String itemType) {
		JobWriterInfo defaultJobWriterInfo = makeDefaultJobWriter(name,
				itemType);

		defaultJobWriterList.add(defaultJobWriterInfo);
	}

	/**
	 * 넘어온 name, WriterType(ItemType)을 이용해 Job Writer를 생성
	 * 
	 * @param name
	 * @param itemType
	 * @return
	 */
	private JobWriterInfo makeDefaultJobWriter(final String name,
			final String itemType) {
		JobWriterInfo jobWriterInfo = new JobWriterInfo();

		jobWriterInfo.setName(name);

		if (defaultJobWriterMap.isFileType(itemType)) {
			jobWriterInfo.setResourceType(JobRWInfo.FILE);
		} else {
			jobWriterInfo.setResourceType(JobRWInfo.DB);
		}

		jobWriterInfo.setItemType(itemType);

		String classValue = defaultJobWriterMap.getClassValue(itemType);
		jobWriterInfo.setClassValue(classValue);

		String resourceDetailType = defaultJobWriterMap.getDetailType(itemType);
		jobWriterInfo.setResourceDetailType(resourceDetailType);

		return jobWriterInfo;
	}

	/**
	 * 기본 Job Reader List에서 Parameter로 넘어온 List에 있는 Job Reader를 제거한다.
	 * 
	 * @param preExistJobReaderList
	 */
	public void removeAllPreExistJobReader(
			List<JobReaderInfo> preExistJobReaderList) {
		if (!NullUtil.isEmpty(preExistJobReaderList)) {
			for (JobReaderInfo preExistJobReader : preExistJobReaderList) {
				removePreExistJobReader(preExistJobReader);
			}
		}
	}

	/**
	 * 기본 Job Reader List에서 Parameter로 넘어온 Job Reader를 제거한다.
	 * 
	 * @param comparedJobReader
	 */
	private void removePreExistJobReader(JobReaderInfo comparedJobReader) {
		JobReaderInfo preExistJobReaderInfo = null;

		for (JobReaderInfo jobReaderInfo : defaultJobReaderList) {
			String originalJobReaderName = jobReaderInfo.getName();
			String comparedJobReaderName = comparedJobReader.getName();

			if (originalJobReaderName.equals(comparedJobReaderName)) {
				preExistJobReaderInfo = jobReaderInfo;
				break;
			}
		}

		defaultJobReaderList.remove(preExistJobReaderInfo);
	}

	/**
	 * 기본 Job Writer List에서 Parameter로 넘어온 List에 있는 Job Writer를 제거한다.
	 * 
	 * @param preExistJobWriterList
	 */
	public void removeAllPreExistJobWriter(
			List<JobWriterInfo> preExistJobWriterList) {
		if (!NullUtil.isEmpty(preExistJobWriterList)) {
			for (JobWriterInfo preExistJobWriter : preExistJobWriterList) {
				removePreExistJobWriter(preExistJobWriter);
			}
		}
	}

	/**
	 * 기본 Job Writer List에서 Parameter로 넘어온 Job Writer를 제거한다.
	 * 
	 * @param comparedJobWriter
	 */
	private void removePreExistJobWriter(JobWriterInfo comparedJobWriter) {
		JobRWInfo preExistJobWriterInfo = null;

		for (JobWriterInfo jobWriterInfo : defaultJobWriterList) {
			String originalJobWriterName = jobWriterInfo.getName();
			String comparedJobWriterName = comparedJobWriter.getName();

			if (originalJobWriterName.equals(comparedJobWriterName)) {
				preExistJobWriterInfo = jobWriterInfo;
				break;
			}
		}

		defaultJobWriterList.remove(preExistJobWriterInfo);
	}

	/**
	 * defaultJobRWList의 값을 가져온다
	 * 
	 * @return the defaultJobRWList
	 */
	public ArrayList<JobReaderInfo> getDefaultJobReaderList() {
		return defaultJobReaderList;
	}

	/**
	 * defaultJobWriterList의 값을 가져온다
	 * 
	 * @return the defaultJobWriterList
	 */
	public ArrayList<JobWriterInfo> getDefaultJobWriterList() {
		return defaultJobWriterList;
	}

}
