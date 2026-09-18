/*
 * Copyright 2011 MOSPA(Ministry of Security and Public Administration).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package egovframework.rte.rdt.pom.unit;

import java.math.BigInteger;

import org.jdom.Element;

import egovframework.rte.rdt.pom.util.StringHelper;

/**
 * Pom 파일에서 사용되는 모든 버전 표현을 담당하고 있는 클래스
 */
public class Version extends PomString implements Comparable<Version> {
	/**
	 * 버전 정보를 담고 있는 프로퍼티 맵
	 */
	protected PomMap properties;
	/**
	 * 실제 버전
	 */
	protected String realVersion;
	/**
	 * 프로퍼티에 지정된 버전인지 여부
	 */
	protected boolean propertyVersion;
	/**
	 * 프로퍼티가 다른 프로퍼티를 참조하는 연쇄를 따라가는 최대 단계. 순환 참조에서 무한 반복을 막는다.
	 */
	private static final int MAX_PROPERTY_DEPTH = 10;
	/**
	 * 버전 문자열을 자리(세그먼트)로 나누는 구분자
	 */
	private static final String SEGMENT_DELIMITER = "[.\\-_]";
	/**
	 * 정식 릴리스와 같은 것으로 취급하는 한정자. 뒤에 붙어도 버전이 낮아지지 않는다.
	 */
	private static final String[] RELEASE_QUALIFIERS = { "ga", "final", "release" };
	/**
	 * 정식 릴리스보다 뒤에 나오는 버전을 뜻하는 한정자(서비스 팩). 뒤에 붙으면 버전이 높아진다.
	 */
	private static final String POST_RELEASE_QUALIFIER = "sp";
	
	/**
	 * 버전 인스턴스를 생성한다.
	 * @param version 버전 문자열
	 */
	public Version(String version) {
		super(version);
	}
	
	/**
	 * 버전 인스턴스를 생성한다.
	 * @param e 버전 엘레멘트
	 */
	public Version(Element e) {
		super(e);
	}
	
	/**
	 * 버전 인스턴스를 생성한다.
	 * @param e 버전 엘레멘트
	 * @param properties 프로퍼티 맵
	 */
	public Version(Element e, PomMap properties) {
		this(e);
		setProperties(properties, e);
	}
	
	/**
	 * 버전 인스턴스를 생성한다. 버전 문자열이 프로퍼티 참조이면 프로퍼티 맵에서 실제 버전을 찾아 설정한다.
	 * @param version 버전 문자열
	 * @param properties 프로퍼티 맵
	 */
	public Version(String version, PomMap properties) {
		super();
		this.properties = properties;
		setContent(version);
	}
	
	/**
	 * 프로퍼티에 기록된 실제 버전을 가져온다. 
	 * @return 실제 버전
	 */
	public String getRealVersion() {
		return realVersion;
	}

	/**
	 * 프로퍼티를 설정한다.
	 * @param properties 프로퍼티 맵
	 * @param e 버전 엘레멘트
	 */
	public void setProperties(PomMap properties, Element e) {
		this.properties = properties;
		setContent(e);
	}
	/**
	 * 프로퍼티 버전인지 여부를 가져온다.
	 * @return 프로퍼티 버전 여부
	 */
	public boolean isPropertyVersion() {
		return propertyVersion;
	}
	/**
	 * 프로퍼티 버전 여부를 설정한다.
	 * @param propertyVersion 프로퍼티 버전
	 */
	public void setPropertyVersion(boolean propertyVersion) {
		this.propertyVersion = propertyVersion;
	}

	/**
	 * 프로퍼티 참조(${...}) 형태이지만 프로퍼티 맵에서 값을 찾지 못해 실제 버전을 알 수 없는지 여부를 가져온다.
	 * @return 프로퍼티를 해석할 수 없으면 true
	 */
	public boolean isUnresolvedProperty() {
		String content = getContent();
		return content != null && StringHelper.isPropertyString(content) && !propertyVersion;
	}

	/**
	 * 이 버전이 주어진 버전보다 오래되었다고 확정할 수 있는지 여부를 가져온다.
	 * 어느 한쪽이라도 실제 버전을 알 수 없으면(버전이 없거나 프로퍼티를 해석할 수 없으면) 판단을 유보하고 false 를 반환한다.
	 * @param other 비교할 버전
	 * @return 실제 버전 기준으로 이 버전이 더 오래되었으면 true
	 */
	public boolean isOlderThan(Version other) {
		if (realVersion == null || other == null || other.realVersion == null) {
			return false;
		}
		if (isUnresolvedProperty() || other.isUnresolvedProperty()) {
			return false;
		}
		return compareTo(other) < 0;
	}

	/**
	 * 버전간의 비교를 수행한다. 프로퍼티 참조가 아닌 실제 버전을 비교한다.
	 *
	 * 버전을 <code>.</code> <code>-</code> <code>_</code> 기준으로 자리별로 나눈 뒤,
	 * 숫자 자리는 문자열이 아니라 수의 크기로 비교한다. 문자열로만 비교하면
	 * <code>6.2.9</code> 가 <code>6.2.11</code> 보다 새 버전으로 판정된다.
	 *
	 * 주의: 이 순서는 equals 와 일관되지 않는다. <code>4.3</code> 과 <code>4.3.0</code>,
	 * <code>5.6.15.Final</code> 과 <code>5.6.15</code> 처럼 표기가 달라도 같은 버전이면 0 을
	 * 반환하지만, Version 은 equals 를 재정의하지 않으므로 두 인스턴스는 서로 같지 않다.
	 * TreeSet, TreeMap 처럼 compareTo 로 동등성을 판단하는 컬렉션에 넣으면 두 표기가 하나로 합쳐진다.
	 * @param o 비교할 버전
	 * @return 비교 결과
	 */
	public int compareTo(Version o) {
		return compareVersion(this.realVersion, o.realVersion);
	}

	/**
	 * 두 버전 문자열을 자리별로 비교한다.
	 * @param v1 기준 버전
	 * @param v2 비교 버전
	 * @return v1 이 더 낮으면 음수, 같으면 0, 더 높으면 양수
	 */
	private static int compareVersion(String v1, String v2) {
		String[] s1 = v1.split(SEGMENT_DELIMITER);
		String[] s2 = v2.split(SEGMENT_DELIMITER);
		int length = Math.max(s1.length, s2.length);
		for (int i = 0; i < length; i++) {
			int result = compareSegment(i < s1.length ? s1[i] : null, i < s2.length ? s2[i] : null);
			if (result != 0) {
				return result;
			}
		}
		return 0;
	}

	/**
	 * 같은 자리의 두 세그먼트를 비교한다. 한쪽에만 있는 자리는 그 자리의 값이
	 * 0 이거나 정식 릴리스 한정자이면 없는 것과 같게 보고(4.3 = 4.3.0 = 4.3.Final),
	 * 그 밖의 숫자나 SP 한정자면 더 높은 버전으로, SNAPSHOT 같은 한정자면 더 낮은 버전으로 본다.
	 * 한정자끼리는 등급(릴리스 이전 &lt; 릴리스 &lt; 릴리스 이후)을 먼저 비교한다.
	 * @param t1 기준 버전의 세그먼트(없으면 null)
	 * @param t2 비교 버전의 세그먼트(없으면 null)
	 * @return t1 이 더 낮으면 음수, 같으면 0, 더 높으면 양수
	 */
	private static int compareSegment(String t1, String t2) {
		if (t1 == null) {
			return -compareWithAbsent(t2);
		}
		if (t2 == null) {
			return compareWithAbsent(t1);
		}
		boolean n1 = isNumeric(t1);
		boolean n2 = isNumeric(t2);
		if (n1 && n2) {
			return compareNumeric(t1, t2);
		}
		if (n1 != n2) {
			// 숫자 자리는 한정자(alpha, RC 등)보다 높은 버전으로 본다.
			return n1 ? 1 : -1;
		}
		int r1 = qualifierRank(t1);
		int r2 = qualifierRank(t2);
		if (r1 != r2) {
			return r1 < r2 ? -1 : 1;
		}
		return compareQualifier(t1, t2);
	}

	/**
	 * 등급이 같은 두 한정자를 비교한다. 한정자를 이름과 뒤따르는 번호로 나눈 뒤 이름이 같으면
	 * 번호를 수의 크기로 비교한다. 통째로 문자열 비교를 하면 <code>RC9</code> 가
	 * <code>RC10</code> 보다 새 버전으로 판정된다.
	 * @param t1 기준 한정자
	 * @param t2 비교 한정자
	 * @return t1 이 더 낮으면 음수, 같으면 0, 더 높으면 양수
	 */
	private static int compareQualifier(String t1, String t2) {
		int p1 = qualifierNameLength(t1);
		int p2 = qualifierNameLength(t2);
		int result = t1.substring(0, p1).compareToIgnoreCase(t2.substring(0, p2));
		if (result != 0) {
			return result;
		}
		String n1 = t1.substring(p1);
		String n2 = t2.substring(p2);
		if (isNumeric(n1) && isNumeric(n2)) {
			return compareNumeric(n1, n2);
		}
		return n1.compareToIgnoreCase(n2);
	}

	/**
	 * 한정자에서 번호가 시작되기 전까지의 이름 부분의 길이를 가져온다.
	 * @param s 한정자
	 * @return 처음 나오는 숫자 앞까지의 길이(숫자가 없으면 전체 길이)
	 */
	private static int qualifierNameLength(String s) {
		int i = 0;
		while (i < s.length() && !Character.isDigit(s.charAt(i))) {
			i++;
		}
		return i;
	}

	/**
	 * 한정자의 등급을 가져온다.
	 * @param s 한정자
	 * @return 릴리스 이전(SNAPSHOT, alpha, RC 등)이면 -1, 정식 릴리스이면 0, 릴리스 이후(SP)이면 1
	 */
	private static int qualifierRank(String s) {
		if (isReleaseQualifier(s)) {
			return 0;
		}
		int nameLength = qualifierNameLength(s);
		String number = s.substring(nameLength);
		if (POST_RELEASE_QUALIFIER.equalsIgnoreCase(s.substring(0, nameLength))
				&& (number.length() == 0 || isNumeric(number))) {
			return 1;
		}
		return -1;
	}

	/**
	 * 상대 버전에는 없는 자리 하나를 비교한다.
	 * @param t 비교할 세그먼트
	 * @return 자리가 없는 쪽보다 높으면 양수, 같으면 0, 낮으면 음수
	 */
	private static int compareWithAbsent(String t) {
		if (isNumeric(t)) {
			return compareNumeric(t, "0");
		}
		return qualifierRank(t);
	}

	/**
	 * 숫자로만 이루어진 두 세그먼트를 수의 크기로 비교한다. 자릿수가 매우 큰 값도 넘침 없이 다룬다.
	 * @param t1 기준 세그먼트
	 * @param t2 비교 세그먼트
	 * @return t1 이 작으면 음수, 같으면 0, 크면 양수
	 */
	private static int compareNumeric(String t1, String t2) {
		return new BigInteger(t1).compareTo(new BigInteger(t2));
	}

	/**
	 * 세그먼트가 숫자로만 이루어져 있는지 여부를 가져온다.
	 * @param s 점검할 세그먼트
	 * @return 숫자로만 이루어져 있으면 true
	 */
	private static boolean isNumeric(String s) {
		if (s.length() == 0) {
			return false;
		}
		for (int i = 0; i < s.length(); i++) {
			if (!Character.isDigit(s.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 세그먼트가 정식 릴리스를 뜻하는 한정자인지 여부를 가져온다.
	 * @param s 점검할 세그먼트
	 * @return GA, Final, RELEASE 중 하나이면 true
	 */
	private static boolean isReleaseQualifier(String s) {
		for (int i = 0; i < RELEASE_QUALIFIERS.length; i++) {
			if (RELEASE_QUALIFIERS[i].equalsIgnoreCase(s)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 버전 내용을 설정한다. 프로퍼티 맵이 있고 내용이 프로퍼티 참조이면 프로퍼티가 다른 프로퍼티를 가리키는 연쇄까지 따라가
	 * 실제 버전을 찾아 설정하고, 그렇지 않으면 내용을 그대로 실제 버전으로 삼는다.
	 * 연쇄 도중 프로퍼티를 찾을 수 없거나 순환 참조이면 해석하지 못한 것으로 두어 isUnresolvedProperty 가 true 가 된다.
	 * @param content 버전 문자열
	 */
	@Override
	public void setContent(String content) {
		super.setContent(content);
		setPropertyVersion(false);
		realVersion = content;
		if (properties == null || content == null) {
			return;
		}
		String resolved = content;
		int depth = 0;
		while (StringHelper.isPropertyString(resolved) && depth < MAX_PROPERTY_DEPTH) {
			PomElement value = properties.getValue(StringHelper.getProperty(resolved));
			if (value == null) {
				return;
			}
			resolved = value.toString();
			depth++;
		}
		if (depth > 0 && !StringHelper.isPropertyString(resolved)) {
			setPropertyVersion(true);
			realVersion = resolved;
		}
	}

	/**
	 * 버전 내용을 설정한다. 엘레멘트가 null 이면(pom 에 version 이 없으면) 아무것도 하지 않는다.
	 * @param element 버전 엘레멘트
	 */
	@Override
	protected void setContent(Element element) {
		if (element == null) {
			return;
		}
		super.setContent(element);
	}
}
