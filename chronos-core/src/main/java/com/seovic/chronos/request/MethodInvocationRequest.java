/*
Copyright 2009 Aleksandar Seovic

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.seovic.chronos.request;


import com.seovic.chronos.Request;

import java.lang.reflect.Method;


/**
 * @author Aleksandar Seovic  2013.04.16
 */
public class MethodInvocationRequest
        extends AbstractRequest
    {
    private final Object target;
    private final Method method;
    private final Object[] arguments;

    public MethodInvocationRequest(Object target, String methodName, Class<?>[] argTypes, Object... arguments)
            throws NoSuchMethodException
        {
        this(target, target.getClass().getMethod(methodName, argTypes), arguments);
        }

    public MethodInvocationRequest(Object target, Method method, Object... arguments)
        {
        this.target = target;
        this.method = method;
        this.arguments = arguments;
        }

    @Override
    public String getName()
        {
        return "method." + target.getClass().getName() + "." + method.getName();
        }

    @Override
    public Object doExecute() throws Exception
        {
        return method.invoke(target, arguments);
        }
    }
