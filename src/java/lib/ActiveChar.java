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
package com.dickimawbooks.texparserlib;

import java.io.IOException;

public abstract class ActiveChar extends Macro implements Expandable
{
   // Character

   public abstract int getCharCode();

   public String toString()
   {
      return String.format("%s[char=%c]",
        getClass().getName(), (char)getCharCode());
   }

   public String format()
   {
      return String.format("%c", (char)getCharCode());
   }

   public TeXObjectList string(TeXParser parser) throws IOException
   {
      return parser.string(toString());
   }

   public abstract Object clone();

   public boolean equals(Object object)
   {
      if (object instanceof ActiveChar)
      {
         return getCharCode() == ((ActiveChar)object).getCharCode();
      }

      return super.equals(object);
   }
}

