/*
    Copyright (C) 2013 Nicola L.C. Talbot
    www.dickimaw-books.com

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation; either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
*/
package com.dickimawbooks.texparserlib.latex2latex;

import java.io.IOException;
import java.util.Vector;

import com.dickimawbooks.texparserlib.*;
import com.dickimawbooks.texparserlib.primitives.*;
import com.dickimawbooks.texparserlib.latex.*;

public class L2LMathGroup extends MathGroup
{
   public L2LMathGroup()
   {
      this(true);
   }

   public L2LMathGroup(boolean isInLine)
   {
      this(isInLine, null, null);
   }

   public L2LMathGroup(boolean isInLine, String openDelim, String closeDelim)
   {
      setInLine(isInLine);
      this.openDelim = openDelim;
      this.closeDelim = closeDelim;
   }

   public Object clone()
   {
      L2LMathGroup math = new L2LMathGroup();
      math.setInLine(isInLine());

      for (TeXObject object : this)
      {
         math.add((TeXObject)object.clone());
      }

      return math;
   }

   public void process(TeXParser parser, TeXObjectList stack)
      throws IOException
   {
      process(parser);
   }

   public void process(TeXParser parser)
      throws IOException
   {
      Writeable writeable = parser.getListener().getWriteable();

      String delim = parser.getMathDelim(isInLine());

      String endDelim;

      int orgMode = parser.getSettings().getCurrentMode();

      if (isInLine())
      {
         endDelim = (closeDelim == null ? delim : closeDelim);
         delim = (openDelim == null ? delim : openDelim);
         parser.getSettings().setMode(TeXSettings.MODE_INLINE_MATH);
      }
      else
      {
         String orgDelim = delim;

         char esc = parser.getEscChar();

         delim = (openDelim == null ? ""+esc+"[" : openDelim);
         endDelim = (closeDelim == null ? ""+esc+"]" : closeDelim);

         LaTeX2LaTeX listener = (LaTeX2LaTeX)parser.getListener();

         StringBuilder argStr = new StringBuilder();

         for (TeXObject obj : this)
         {
            if (obj instanceof Obsolete)
            {
               argStr.append(((Obsolete)obj).getOriginalCommand().toString(parser));
            }
            else
            {
               argStr.append(obj.toString(parser));
            }
         }

         if (!delim.equals(openDelim) && !endDelim.equals(closeDelim))
         {
            listener.substituting( 
                orgDelim+argStr+orgDelim, delim+argStr+endDelim);
         }

         parser.getSettings().setMode(TeXSettings.MODE_DISPLAY_MATH);
      }

      writeable.write(delim);

      while (size() > 0)
      {
         TeXObject object = pop();

         if (object instanceof Obsolete
           && ((Obsolete)object).getOriginalCommand()
                   instanceof TeXFontDeclaration
            )
         {
            ControlSequence original = 
               ((Obsolete)object).getOriginalCommand();

            LaTeX2LaTeX listener = (LaTeX2LaTeX)parser.getListener();

            ControlSequence cs = listener.getControlSequence(
               "math"+original.getName());

            String replacement = cs.toString(parser);
            listener.substituting(original.toString(parser), replacement);

            Group grp = parser.getListener().createGroup();

            while (size() > 0)
            {
               grp.add(pop());
            }

            cs.process(parser, grp);
         }
         else if (object instanceof Ignoreable
               || object instanceof WhiteSpace)
         {
            writeable.write(object.toString(parser));
         }
         else
         {
            object.process(parser, this);
         }
      }

      parser.getSettings().setMode(orgMode);

      writeable.write(endDelim);

      TeXObject nextObj = parser.popStack();

      while (nextObj != null)
      {
         if (!(nextObj instanceof Ignoreable))
         {
            nextObj.process(parser);
            break;
         }

         writeable.write(nextObj.toString(parser));
      }
   }

   private String openDelim, closeDelim;
}

