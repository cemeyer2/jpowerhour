/**
 * This sub-package of the iTunes consistency checker deals with the control
 * file and its parsing.
 * <p>
 * The control file is line-based; each line contains a directive (though some
 * directives may be split across lines using a backslash continuation marker).
 * The driver for the parsing is in {@link ncrossley.itunes.controls.Controls}.
 * For each type of directive, the parser contructs an instance of an appropriate
 * subclass of {@link ncrossley.itunes.controls.AbstractControl} to handle the
 * directive.
 * <p>
 * @author Copyright (c) 2005-2008 Nick Crossley; All rights reserved.
 * @since 1.0
 */
package ncrossley.itunes.controls;
