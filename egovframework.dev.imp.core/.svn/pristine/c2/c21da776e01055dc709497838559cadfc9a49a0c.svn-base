/*
 * Copyright 2008-2009 the original author or authors.
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
package egovframework.dev.imp.core.utils;

import java.util.regex.Pattern;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.widgets.Label;

/**
 * 문자열 처리 유틸리티 클래스
 * 
 * @author 개발환경 개발팀 이흥주
 * @since 2009.06.01
 * @version 1.0
 * @see <pre>
 * &lt;&lt; 개정이력(Modification Information) &gt;&gt;
 *   
 * 수정일		수정자	수정내용
 * ----------	------	---------------------------
 * 2009.02.20	이흥주	최초 생성
 * 2012.09.06	조용현	함수 추가
 * 
 * </pre>
 */
public class StringUtil {

	public static String ENG_PATTERN = "a-zA-Z";

	public static String KOR_PATTERN = "가-힣ㄱ-ㅎㅏ-ㅣ";

	public static String NUM_PATTERN = "0-9";

	/**
	 * null을 빈문자열로 대체
	 * 
	 * @param value
	 * @return
	 */
	public static String nvl(String value) {
		return value == null ? "" : value;
	}

	public static boolean doesStringMatchWithPatten(String pattern,
			String string) {
		if (!Pattern.matches("[" + pattern + "]+", string)) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean doesCharacterBelongToPattern(String pattern,
			char character) {
		String string = Character.toString(character);

		if (!Pattern.matches("[" + pattern + "]+", string)) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean hasEmptySpace(String string) {
		if (doesStringHasSignal(' ', string)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isSignalFirstOrLast(char signal, String string) {
		if (NullUtil.isEmpty(string)) {
			return false;
		} else {
			char[] characters = string.toCharArray();
			if (characters[0] == signal
					|| characters[characters.length - 1] == signal) {
				return true;
			} else {
				return false;
			}
		}
	}

	public static boolean isSignalsFirstOrLast(String signals, String string) {
		if (NullUtil.isEmpty(signals)) {
			return false;
		} else {
			char[] charSignals = signals.toCharArray();

			if (!NullUtil.isNull(charSignals)) {
				for (char signal : charSignals) {
					if (isSignalFirstOrLast(signal, string)) {
						return true;
					}
				}
			}

			return false;
		}
	}

	public static boolean doesCharacterOfStringBelongToPatternAtleastOne(
			String pattern, String string) {
		char[] chars = string.toCharArray();
		for (int i = 0; i < chars.length; i++) {
			if (doesCharacterBelongToPattern(pattern, chars[i])) {
				return true;
			}
		}

		return false;
	}

	public static boolean doesStringHasSignalsAtLeastOneCharacter(
			String signals, String string) {
		char[] charSignals = signals.toCharArray();
		for (char signal : charSignals) {
			if (doesStringHasSignal(signal, string)) {
				return true;
			}
		}
		return false;
	}

	public static boolean doesStringHasSignal(char signal, String string) {
		if (string.indexOf(signal) > -1) {
			return true;
		} else {
			return false;
		}
	}

	public static String returnEmptyStringIfNull(String string) {
		if (NullUtil.isEmpty(string)) {
			return "";
		} else {
			return string;
		}
	}

	public static boolean isStringStartWithNumber(String string) {
		if (!NullUtil.isEmpty(string)) {
			char firstChar = string.charAt(0);
			return Character.isDigit(firstChar);
		} else {
			return false;
		}
	}

	public static boolean hasKorean(String string) {
		return doesCharacterOfStringBelongToPatternAtleastOne(KOR_PATTERN, string);
	}

	public static boolean isAlphabetAndNumber(String string) {
		return doesStringMatchWithPatten(ENG_PATTERN + NUM_PATTERN, string);
	}

	/**
	 * < > / ? " : * | \ 기호여부 확인
	 * 
	 * @param string
	 * @return
	 */
	public static boolean hasInvalidClassFileSignal(String string) {
		String invalidChar = "<>/?:*|" + "\"" + "\\";

		if (doesStringHasSignalsAtLeastOneCharacter(invalidChar, string)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isBatchJobBeanIDAvailable(String beanID) {
		String signals = ".-";
		String pattern = ENG_PATTERN + NUM_PATTERN + signals;

		if (doesStringMatchWithPatten(pattern, beanID)) {
			if (isSignalsFirstOrLast(signals, beanID)
					|| isStringStartWithNumber(beanID)) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
	
	public static void setLabelStringBold(Label label){
		label.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DIALOG_FONT));
	}

}
