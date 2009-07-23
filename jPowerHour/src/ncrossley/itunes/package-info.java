/**
 * This package and its sub-packages contain a program to check that an iTunes
 * library is complete and consistent with a set of music files on disk.
 * <p>
 * The exact set of checks performed is defined by a control file; the syntax of
 * the control file is defined in {@link ncrossley.itunes.controls.Controls}.
 * Typical checks include checking that every track in the iTunes library refers
 * to an existing file in the given music directory, and that every file in the
 * music directory hierarchy is included in the library, and in at least one
 * playlist.
 * <p>
 * The program may be run both from the command line or the GUI. The CLI main
 * entry point is in the class {@link ncrossley.itunes.ITunes}, while the GUI
 * entry point is in {@link ncrossley.itunes.gui.ITunesGUI}.
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 * @since 1.0
 */
package ncrossley.itunes;
