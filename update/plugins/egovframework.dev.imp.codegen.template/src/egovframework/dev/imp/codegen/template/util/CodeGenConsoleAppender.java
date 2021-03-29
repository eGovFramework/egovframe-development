/*
 * Copyright 2008-2009 MOSPA(Ministry of Security and Public Administration).
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
package egovframework.dev.imp.codegen.template.util;

import org.apache.log4j.Layout;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.helpers.LogLog;

/**
 * 템플릿 기반 코드젠 로그를 콘솔로 출력하기 위한 Appender 클래스
 * <p><b>NOTE:</b> 로그 처리  
 * @author 개발환경 개발팀 윤수열
 * @since 2009.08.10
 * @version 1.0
 * @see
 *
 * <pre>
 *  == 개정이력(Modification Information) ==
 *   
 *   수정일      수정자           수정내용
 *  -------    --------    ---------------------------
 *   2009.08.10  윤수열          최초 생성
 *
 * </pre>
 */
public class CodeGenConsoleAppender extends WriterAppender {
	
	/**
	 * 생성자
	 */
	public CodeGenConsoleAppender() {
		target = "System.out";
		follow = false;
		LogUtil.console();
	}

	/**
	 * 생성자
	 * @param layout
	 */
	public CodeGenConsoleAppender(Layout layout) {
		this(layout, "System.out");
		LogUtil.console();
	}

	/**
	 * 생성자
	 * @param layout
	 * @param target
	 */
	public CodeGenConsoleAppender(Layout layout, String target) {
		this.target = "System.out";
		follow = false;
		setLayout(layout);
		setTarget(target);
		activateOptions();
	}

	/**
	 * 출력 타겟 세팅하기 
	 * @param value
	 */
	public void setTarget(String value) {
		String v = value.trim();
		if ("System.out".equalsIgnoreCase(v))
			target = "System.out";
		else if ("System.err".equalsIgnoreCase(v))
			target = "System.err";
		else
			targetWarn(value);
	}

	/**
	 * 출력 타겟 가져오기
	 * @return
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * 경고 메시지의 출력 
	 * @param val
	 */
	void targetWarn(String val) {
		LogLog.warn("[" + val + "] should be System.out or System.err.");
		LogLog.warn("Using previously set target, System.out by default.");
	}

	/* 
	 * 로그 옵션 
	 * (non-Javadoc)
	 * @see org.apache.log4j.WriterAppender#activateOptions()
	 */
	public void activateOptions() {

		if (follow) {
			if (target.equals("System.err")) {
				setWriter(createWriter(LogUtil.stdOutStream));
			} else {
				setWriter(createWriter(LogUtil.stdOutStream));
			}
		} else if (target.equals("System.err")) {
			setWriter(createWriter(LogUtil.stdOutStream));
		} else {
			setWriter(createWriter(LogUtil.stdOutStream));
		}
		super.activateOptions();
	}

	/* 
	 * 출력 닫기
	 * (non-Javadoc)
	 * @see org.apache.log4j.WriterAppender#closeWriter()
	 */
	protected final void closeWriter() {
		if (follow)
			super.closeWriter();
	}

	/**
	 * 이어서 출력하는 옵션 세팅하기
	 * @param newValue
	 */
	public final void setFollow(boolean newValue) {
		follow = newValue;
	}

	/**
	 * 이어서 출력하는 옵션 가져오기
	 * @return
	 */
	public final boolean getFollow() {
		return follow;
	}
	/** 시스템 아웃 */
	public static final String SYSTEM_OUT = "System.out";
	/** 시스템 에러 */
	public static final String SYSTEM_ERR = "System.err";
	/** 출력 타겟 */
	protected String target;
	/** 이어서 출력 옵션 */
	private boolean follow;

}
