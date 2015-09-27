#!/bin/bash -e
#
#
# Startup script for Datafari
#
#

if (( EUID != 0 )); then
   echo "You need to be root to run this script." 1>&2
   exit 100
fi


../../../cassandra/bin/cqlsh -f ../../../bin/config/cassandra/tables
../../../cassandra/bin/cqlsh -f create-admin-dev.txt

echo test