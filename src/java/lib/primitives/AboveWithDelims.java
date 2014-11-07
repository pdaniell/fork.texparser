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
package com.dickimawbooks.texparserlib.primitives;

import java.io.IOException;
import java.io.Writer;

import com.dickimawbooks.texparserlib.*;

public class AboveWithDelims extends MidControlSequence
{
   public AboveWithDelims()
   {
   }

   public String getName()
   {
      return "abovewithdelims";
   }

   public Object clone()
   {
      return new AboveWithDelims();
   }

   public void process(TeXParser parser, 
      TeXObjectList before, TeXObjectList following)
      throws IOException
   {
      if (!parser.isMathMode())
      {
         throw new TeXSyntaxException(
           parser,
           TeXSyntaxException.ERROR_NOT_MATH_MODE,
           getName());
      }

      TeXObject first = null;
      TeXObject second = null;

      if (following == null || following.size() == 0)
      {
         throw new TeXSyntaxException(
          parser,
          TeXSyntaxException.ERROR_MISSING_PARAM,
          getName());
      }

      first = following.popStack();

      if (following.size() == 0)
      {
         throw new TeXSyntaxException(
          parser,
          TeXSyntaxException.ERROR_MISSING_PARAM,
          toString(parser));
      }

      second = following.popStack();

      if (following.size() == 0)
      {
         throw new TeXSyntaxException(
          parser,
          TeXSyntaxException.ERROR_MISSING_PARAM,
          second.toString(parser));
      }

      TeXDimension dimen = following.popDimension(parser);

      parser.getListener().abovewithdelims(first, second, dimen, before,
        following);
   }
}
