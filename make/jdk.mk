# ----------------------------------------------------------------------------------------------------------------------
# JDK: Paths and Variables
# ----------------------------------------------------------------------------------------------------------------------

JAVA_VERSION ?= 1.6+

# Darwin, use java_home exe
# or fallback to known path if exists
ifeq ($(OS),Darwin)
JAVA_HOME   ?= $(shell /usr/libexec/java_home -v "$(JAVA_VERSION)")
JAVA_HOME   ?= $(wildcard /Library/Java/JavaVirtualMachines/jdk1.8.0_73.jdk/Contents/Home)
endif

# Linux, use common path
JAVA_HOME   ?= /usr/lib/jvm/java-8-oracle

export JAVA_HOME

JAVA         ?= $(JAVA_HOME)/bin/java
JAVAC        ?= $(JAVA_HOME)/bin/javac



# ----------------------------------------------------------------------------------------------------------------------
# JDK: Helper Rules
# ----------------------------------------------------------------------------------------------------------------------
print-java:
	@echo $(OS) JAVA_HOME=$(JAVA_HOME) JAVA_VERSION=$(JAVA_VERSION)
