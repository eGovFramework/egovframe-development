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
package egovframework.dev.imp.dbio.util;

import org.apache.log4j.Layout;
import org.apache.log4j.WriterAppender;
import org.apache.log4j.helpers.LogLog;

public class DBIOConsoleAppender extends WriterAppender {
	
	   public DBIOConsoleAppender()
	    {
	        target = "System.out";
	        follow = false;
       		LogUtil.Console();
	    }

	    public DBIOConsoleAppender(Layout layout)
	    {
	        this(layout, "System.out");
        	LogUtil.Console();	    	
	    }

	    public DBIOConsoleAppender(Layout layout, String target)
	    {
	        this.target = "System.out";
	        follow = false;
	        setLayout(layout);
	        setTarget(target);
	        activateOptions();
	    }

	    public void setTarget(String value)
	    {
	        String v = value.trim();
	        if("System.out".equalsIgnoreCase(v))
	            target = "System.out";
	        else
	        if("System.err".equalsIgnoreCase(v))
	            target = "System.err";
	        else
	            targetWarn(value);
	    }

	    public String getTarget()
	    {
	        return target;
	    }

	    void targetWarn(String val)
	    {
	        LogLog.warn("[" + val + "] should be System.out or System.err.");
	        LogLog.warn("Using previously set target, System.out by default.");
	    }

	    public void activateOptions()
	    {

	        if(follow)
	        {
	            if(target.equals("System.err")){
	            	setWriter(createWriter(LogUtil.stdOutStream));
	            }else{
	                setWriter(createWriter(LogUtil.stdOutStream));
	            }
	        } else
	        if(target.equals("System.err")){
	            setWriter(createWriter(LogUtil.stdOutStream));
	        }
	        else{
	            setWriter(createWriter(LogUtil.stdOutStream));
	        }
	        super.activateOptions();
	    }
	    protected final void closeWriter()
	    {
	        if(follow)
	            super.closeWriter();
	    }
	    public final void setFollow(boolean newValue)
	    {
	        follow = newValue;
	    }

	    public final boolean getFollow()
	    {
	        return follow;
	    }
	    public static final String SYSTEM_OUT = "System.out";
	    public static final String SYSTEM_ERR = "System.err";
	    protected String target;
	    private boolean follow;


}
