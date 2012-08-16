#/bin/sh

git clone https://github.com/jheintz/sample-hadoop-testing.git
cd sample-hadoop-testing.git

git checkout step1  # initial MRUnit tests
mvn test
# eclipse: show mapper
# eclipse: show reducer
# eclipse: show unit test
# eclipse: try open hadoop source...

git checkout step2  # Use Cloudera repo for sources
mvn test
# eclipse show pom
# eclipse: open hadoop source

git checkout step3  # parameterized many tests
# eclipse test ManyWordCountTest.java

git checkout step4  # parameterized non-mr tests for speed
# eclipse test ManyWordTest

git checkout step5  # add cluster test
# eclipse show HadoopClusterBase
# eclipse test ClusterTest

git checkout step6  # add parameterized cluster test
# eclipse test MultipleClusterTest

git checkout step7  # split unit and integration tests
# eclipse show test names
# eclipse show pom failsafe
mvn test
mvn verify
