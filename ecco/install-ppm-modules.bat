@echo off

REM This batch file sets up the repositories and modules for ActivePerl.

ppm repo add tersesystems.com http://tersesystems.com/code/eccoapi
ppm repo add brides.org http://www.bribes.org/perl/ppm
ppm install Win32-Ecco-Sample-GTD
ppm install Win32-Ecco-Export-ICal