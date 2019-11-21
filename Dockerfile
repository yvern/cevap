FROM rightmesh/ubuntu-openjdk:18.04

ENV LD_LIBRARY_PATH=/opt/intel/lib/intel64:/opt/intel/mkl/lib/intel64:${LD_LIBRARY_PATH}

RUN apt update && apt install wget -y
RUN wget https://apt.repos.intel.com/intel-gpg-keys/GPG-PUB-KEY-INTEL-SW-PRODUCTS-2019.PUB
RUN apt-key add GPG-PUB-KEY-INTEL-SW-PRODUCTS-2019.PUB
RUN sh -c 'echo deb https://apt.repos.intel.com/mkl all main > /etc/apt/sources.list.d/intel-mkl.list'
RUN apt update && apt install intel-mkl-64bit-2019.5-075 -y

RUN wget -O /bin/lein https://raw.githubusercontent.com/technomancy/leiningen/stable/bin/lein
RUN chmod a+x /bin/lein

WORKDIR /opt
COPY src src
COPY test test
COPY project.clj .
RUN lein deps
RUN lein test
RUN lein uberjar

RUN mv target/uberjar/cevap-0.1.0-SNAPSHOT-standalone.jar cevap.jar

CMD [ "java", "-jar", "cevap.jar" ]