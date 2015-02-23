#!/bin/bash -e

# Licensed to the Apache Software Foundation (ASF) under one or more
# contributor license agreements.  See the NOTICE file distributed with
# this work for additional information regarding copyright ownership.
# The ASF licenses this file to You under the Apache License, Version 2.0
# (the "License"); you may not use this file except in compliance with
# the License.  You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.

if [[ $OSTYPE == "cygwin" ]] ; then
    OPTIONSFILE="hsqldb-options.env.win"
else
    OPTIONSFILE="hsqldb-options.env.unix"
fi

#Make sure environment variables are properly set
if [ -e "$JAVA_HOME"/bin/java ] ; then
    if [ -f ./propertiesDev.xml ] ; then
        # Build the global options
        OPTIONS=$(cat "$OPTIONSFILE")
        
        "$JAVA_HOME/bin/java" $OPTIONS org.hsqldb.Server -database.0 "file:extdb;hsqldb.tx=mvcc;hsqldb.cache_file_scale=512" -dbname.0 xdb
        exit $?
        
    else
        echo "Working directory contains no properties.xml file." 1>&2
        exit 1
    fi

else
    echo "Environment variable JAVA_HOME is not properly set." 1>&2
    exit 1
fi
