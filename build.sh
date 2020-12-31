#!/bin/bash

MAVEN_OPTS="-Xverify:none -XX:+TieredCompilation -XX:TieredStopAtLevel=1"
mvn -T 1C install -am -offline