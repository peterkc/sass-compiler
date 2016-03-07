# ----------------------------------------------------------------------------------------------------------------------
# Notes
# ----------------------------------------------------------------------------------------------------------------------
# $@ 	The name of the current target.
# $?	The list of dependencies newer than the target.
# $<	The name of the dependency file, as if selected by make for use with an implicit rule.
# $*	The base name of the current target (the target name stripped of its suffix).
# $%	the name of the item/object being processed

# $^ gives you all dependencies, regardless of whether they are more recent than the target.
# Duplicate names, however, will be removed. This might be useful if you produce transient output.

# $+ is like $^, but it keeps duplicates and gives you the entire list of dependencies in the order they appear.

# Variable Assignment
# var  = value - Recursive substitution ($(var) expands to value, like a macro); this is the only portable assignment.
# var := value - Simple assignment ($(var) acts like a conventional variable)
# var ?= value - Conditional assignment (value is assigned to var only if var is not defined)
# var += value - Append (value is appended to var's current value)

# Syntax
#
# Rule: Dependencies
#  Actions

# ----------------------------------------------------------------------------------------------------------------------
# Prologue
# ----------------------------------------------------------------------------------------------------------------------

MAKE  := /usr/bin/make
SHELL := /bin/bash

.SHELLFLAGS := -eu -o pipefail -c
.DEFAULT_GOAL := silent
.SUFFIXES:
.DELETE_ON_ERROR:

# Allow % to match multiple directories.
percent_subdirs := 1

OS := $(shell uname)

# ----------------------------------------------------------------------------------------------------------------------
# Make Version Check
# ----------------------------------------------------------------------------------------------------------------------
MAKE_VERSION := $(shell $(MAKE) --version)
ifneq ($(firstword $(MAKE_VERSION)),GNU)
$(error Use GNU Make)
endif


# ----------------------------------------------------------------------------------------------------------------------
# Alias
# ----------------------------------------------------------------------------------------------------------------------
SCRIPTS_DIR ?= .
CURL        ?= curl
RM          ?= rm
RSYNC       ?= rsync
MKDIR       ?= mkdir
ECHO        ?= echo -e
GIT         ?= git

# ----------------------------------------------------------------------------------------------------------------------
# Java Variables and Settings (see make/jdk.mk)
# ----------------------------------------------------------------------------------------------------------------------
JAVA_VERSION := 1.8+


# ----------------------------------------------------------------------------------------------------------------------
# Maven Variables and Settings
# ----------------------------------------------------------------------------------------------------------------------
MAVEN_VERSION  ?= 3.3.9
MAVEN_OPTS     ?=
MAVEN_OPTS     += -T 1C
MAVEN_OPTS     += --batch-mode
MAVEN_OPTS     += --fail-fast
#MAVEN_OPTS  += --log-file=logs/maven.log


# ----------------------------------------------------------------------------------------------------------------------
# Includes: Common and Project
# ----------------------------------------------------------------------------------------------------------------------
include ./make/gmsl
include ./make/helper.mk
include ./make/common.mk
include ./make/jdk.mk
include ./make/maven.mk

INCLUDE_FILES ?=
include $(INCLUDES)


# ----------------------------------------------------------------------------------------------------------------------
# Project rules
# ----------------------------------------------------------------------------------------------------------------------
.PHONY: deps clean compile

deps:
	$(MAVEN) $(MAVEN_OPTS) dependency:resolve

clean:
	$(MAVEN) $(MAVEN_OPTS) clean

compile:
	$(MAVEN) $(MAVEN_OPTS) compile

submodules:
	$(GIT) submodule update --init --recursive

# ----------------------------------------------------------------------------------------------------------------------
# Test rules
# ----------------------------------------------------------------------------------------------------------------------
.PHONY: test-vaadin test-glide test-glide-media

test-glide: TEST_GROUP ?= com/glide/scss
test-glide: MAVEN_OPTS += -DtestGroup="com/vaadin"
test-glide:
	$(MAVEN) $(MAVEN_OPTS) test

test-glide-media: MAVEN_OPTS += -DtestGroup="com/glide/scss"
test-glide-media: MAVEN_OPTS += -Dsass.spec.dir="sass-spec/spec/scss/media-with-interpolation"
test-glide-media:
	$(MAVEN) $(MAVEN_OPTS) test
