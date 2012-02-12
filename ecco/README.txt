There are two systems embedded in this converter -- one of them converts
Shadow XML to and from OML, and another one (written in Perl) converts
OML to and from Ecco format, using Windows DDE.

The properties file format enables a Shadow XML file to be transferred 
to an Ecco Folder, or vice versa.

There is only export / import functionality.  You cannot use this 
converter to synchronize between files.  

The way that I use this is to have an Inbasket folder, which imports from 
Shadow.  (See inbasket.properties for details.)  I can also use the 
folder to type stuff in directly.  Note that the destructive flag means
that items in Shadow are deleted once they have been transferred into Ecco.

Then I also have Action and Someday folders in Ecco.  These are export
only.  Changes made in Ecco will appear in Shadow, but any changes
made in Shadow will not sync back up.  (See the action.properties and
someday.properties files for details.)

Finally, I have a daily diary which works much the same way as the 
inbasket.

The importer is smart enough to understand Shadow Tags.  If you define
a tag in Shadow, and you have the equivalent folder in Ecco, then any
items defined with that tag will be assigned to the appropriate 
Ecco folder on import.  This is very effective when using the inbasket.
