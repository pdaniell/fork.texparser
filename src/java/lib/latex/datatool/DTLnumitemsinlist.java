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
package com.dickimawbooks.texparserlib.latex.datatool;

import java.io.IOException;

import com.dickimawbooks.texparserlib.*;
import com.dickimawbooks.texparserlib.primitives.IfTrue;
import com.dickimawbooks.texparserlib.latex.*;

public class DTLnumitemsinlist extends ControlSequence
{
   public DTLnumitemsinlist()
   {
      this("DTLnumitemsinlist");
   }

   public DTLnumitemsinlist(String name)
   {
      super(name);
   }

   public Object clone()
   {
      return new DTLnumitemsinlist(getName());
   }

   public void process(TeXParser parser, TeXObjectList stack)
     throws IOException
   {
      TeXObject list;

      if (parser == stack)
      {
         list = parser.popNextArg();
      }
      else
      {
         list = stack.popArg(parser);
      }

      CsvList csvList = null;

      if (list instanceof CsvList)
      {
         csvList = (CsvList)list;
      }
      else if (list instanceof TeXObjectList
         && ((TeXObjectList)list).size() == 0
         && ((TeXObjectList)list).firstElement() instanceof CsvList)
      {
         csvList = (CsvList)((TeXObjectList)list).firstElement();
      }
      else if (list instanceof Expandable)
      {
         TeXObjectList expanded;

         if (parser == stack)
         {
            expanded = ((Expandable)list).expandonce(parser);
         }
         else
         {
            expanded = ((Expandable)list).expandonce(parser, stack);
         }

         if (expanded != null)
         {
            list = expanded;
         }

         if (list instanceof TeXObjectList
            && ((TeXObjectList)list).size() == 0
            && ((TeXObjectList)list).firstElement() instanceof CsvList)
         {
            csvList = (CsvList)((TeXObjectList)list).firstElement();
         }
      }

      ControlSequence cmd = stack.popControlSequence(parser);

      String csName = cmd.getName();

      if (csvList == null)
      {
         csvList = CsvList.getList(parser, list);
      }

      int n = 0;

      ControlSequence ifCs = parser.getControlSequence("ifDTLlistskipempty");
      boolean skipEmpty = (ifCs instanceof IfTrue);

      for (int i = 0; i < csvList.size(); i++)
      {
         TeXObject obj = csvList.getValue(i);

         if (!skipEmpty || !(obj instanceof TeXObjectList 
                && ((TeXObjectList)obj).size() == 0))
         {
            n++;
         }
      }

      parser.putControlSequence(true, new GenericCommand(true,
         csName, null, new UserNumber(n)));
   }

   public void process(TeXParser parser)
     throws IOException
   {
      process(parser, parser);
   }

}
