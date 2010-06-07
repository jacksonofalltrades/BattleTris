#!/bin/bash
export CLASSPATH=/usr/local/lib/starwarp_utils.jar:/usr/local/lib/battletris.jar
java -Dbt.directenv=true -Djava.security.policy="/usr/local/battletris/battletris.security.policy" battletris.BattleTrisApp $1 240 480 $2 $3 
