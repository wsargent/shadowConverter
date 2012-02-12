INTRODUCTION

This is the README for shadowConverter.

This is a solution for integrating Ecco Pro and Shadow Plan.  This is something that I wrote myself, so apologies
if it is technical in nature.

PREREQUISITES

This guide assumes you have ShadowPlan 4.x and the latest version of Ecco Pro.  It has not been tested on
the Ecco Extension binary.  In addition, you will need some version of both Perl and Java, and be comfortable
with messing around with a command prompt.

You will need ActiveState Perl to start with.  The URL is http://aspn.activestate.com/ASPN/Perl/Downloads/
Download that and make sure you can run 'perl -version' from the command line before continuing.

Java will likewise be easy to install. Go to java.com and pick up the latest install.

Once you've done that, go to the shadowConverter installation and look at the ecco directory. You'll find a Perl
shellscript called syncall.pl, which you can edit to your tastes. However, this perl script depends
on the Win32::Ecco library, which is not included by default.

You can install the Win32::Ecco library by going to the ecco directory and run install-ppm-modules.bat,
or you can install it by hand. If you run it by hand, then you should run the following commands
from a command prompt:

    * ppm repository add tersesystems.com http://tersesystems.com/code/eccoapi
    * ppm repository add bribes.org http://www.bribes.org/perl/ppm
    * ppm install Win32-Ecco-Sample-GTD

CONFIGURATION

You now have all the pieces you need.

You'll also find some properties files in the ecco directory. These determine what shadow XML files
will be mapped to which Ecco folders. These files have the following format:

file=d:/usr/shadowConverter/Someday.xml  # the OML or Shadow XML file to be created.
user=wsarge                              # the palm username.
shadowName=someday                       # the shadow file
destructive=true                         # removes previous items.  I use this for the inbasket.
importer=oml                             # Import from OML (Ecco generated)
exporter=shadow                          # Exports to Shadow XML format.

The syncall.pl perl script depends on a couple of variables which you can set by hand or set using environment
variables. The script won't work without these, so make sure you set them.  Note that the user name is used as
the directory found in "C:\Program Files\Palm\<username>".  This may be truncated; my username is wsargent, but the
directory is 'wsarge', so don't assume they're the same thing.

my $username = $ENV{'SHADOWCONV_USERNAME'};
my $shadowConvHome = $ENV{'SHADOWCONV_HOME'};

Finally, you will want to tweak the way that Shadow Plan does its synchronization with Ecco Pro.  Right click
on the Hotsync Manager, then pull up the Custom dialog box.  You should see the Shadow Conduit.

1. Change the conduit so it says "Synchronize: Desktop item overrides handheld" and
2. Check the "Set as default" checkbox so this is always the case.

You don't need to know why, but you can read the discussion at the URL if you have a morbid
interest in such things: http://groups.yahoo.com/group/shadow-discuss/message/19336.

Once you've done that, run the syncall.pl script from the command line with 'perl syncall.pl'  Then do a
hotsync, and check that what was defined to be exported from Shadow is in Ecco, and what was defined
to be imported from Ecco is in Shadow.

RUNNING

Once you've verified that it works from the command line, you can integrate it into Ecco Pro by setting up a
launch item with the script.  In my case, this looks like:

"d:\usr\perl\bin\perl.exe d:\usr\shadowConverter\ecco\syncall.pl"

with the "Launch as Program" option set.
You may get various command prompts popping up at this point.  You can eliminate these by tweaking the "perl.exe"
to be "wperl.exe" and the "java.exe" in syncall.pl to be "javaw.exe".  However, note that you will not see how
the conversion went in that case.

You can also get this to run as a Windows Scheduled Task, if you are not into launching it manually.  I usually
set mine up to run when idle.

BUGS

ActivePerl makes use of a package called Time::Local, which makes some assumptions that are not valid for
pre-epoch dates on Windows.   If ever you see something like the following:

"Use of uninitialized value in integer addition (+) at d:/usr/Perl/lib/Time/Local.pm line 76."

Then it's because it hiccuped.  You can fix this by using PPM to download the latest version of Time::Local
and copying it into the lib/Time directory by hand (PPM will install it into the site/lib directory, so there's
sadly no automated fix).

CONTACT

Please contact me at will.sargent@gmail.com if you run into problems.