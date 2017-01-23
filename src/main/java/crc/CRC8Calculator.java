/******************************************************************************
 *
 * Jacksum version 1.7.0 - checksum utility in Java
 * Copyright (C) 2001-2006 Dipl.-Inf. (FH) Johann Nepomuk Loefflmann,
 * All Rights Reserved, http://www.jonelo.de
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 *
 * E-mail: jonelo@jonelo.de
 *
 *****************************************************************************
 *
 * The original CRC8.java is from the project
 * http://sourceforge.net/projects/jflac
 * (org.kc7bfi.jflac.util.CRC8.java)
 * which is distributed under the LGPL
 *
 * the jFLAC is derived from the FLAC lib
 *
 *
 * libFLAC - Free Lossless Audio Codec library
 * Copyright (C) 2000,2001,2002,2003  Josh Coalson
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA.
 *
 *****************************************************************************/

package crc;


import jonelo.jacksum.JacksumAPI;
import jonelo.jacksum.algorithm.AbstractChecksum;

import java.security.NoSuchAlgorithmException;

/**
 * A class that can be used to compute the Crc8 of a data stream.
 */
public class CRC8Calculator implements CRCCalculator {

    private static CRC8Calculator instance;
    private CRC8Calculator(){
    }
    public static CRC8Calculator getInstance(){
        if (instance == null){
            instance = new CRC8Calculator();
        }
        return instance;
    }
    public long calculateCRC(String message) throws NoSuchAlgorithmException {
        return this.calculateCRC8(message);
    }
    private long calculateCRC8(String message) throws NoSuchAlgorithmException{
        AbstractChecksum checksum = JacksumAPI.getChecksumInstance("crc-8");
        checksum.update(message.getBytes());
        return checksum.getValue();

    }
}