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

public class Par implements TeXObject
{
   public Par()
   {
   }

   public Object clone()
   {
      return new Par();
   }

   public String toString(TeXParser parser)
   {
      String eol = System.getProperty("line.separator", "\n");
      return eol+eol;
   }

   public String toString()
   {
      return "\\par ";
   }

   public TeXObjectList string(TeXParser parser)
     throws IOException
   {
      return parser.string(""+parser.getEscChar()+"par");
   }

   public void process(TeXParser parser, TeXObjectList list)
      throws IOException
   {
   }

   public void process(TeXParser parser)
      throws IOException
   {
   }
}

