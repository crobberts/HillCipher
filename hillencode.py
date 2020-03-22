#!/usr/bin/python

import argparse
import sys

def parseargs():
    parser = argparse.ArgumentParser(add_help=True, prog = __file__, 
    description='Convert a file to a sequence of integers')

    parser.add_argument('--coding', choices=['alpha', 'ascii', 'binary'], default='ascii',
                        help='Encoding mode: alpha for "A"-"Z" (radix 26), \
                        ascii for any ASCII text (radix 128), \
                        binary for any data (radix 256). Default is ascii.')
    parser.add_argument('input', help='input file to convert')
    parser.add_argument('output', help='output file with integer sequence', nargs='?')
    _parseargs = parser.parse_args()   
    return _parseargs

def encode_alpha(byte):
    if byte.isspace():
        return ''
    if not (byte >= 'A' and byte <= 'Z'):
        raise ValueError
    return str(ord(byte) - ord('A'))

def encode_ascii(byte):
    if ord(byte) > 127:
        raise ValueError
    return str(ord(byte))

def encode_binary(byte):
    return str(ord(byte))

def encode(encoder, infile, outfile):
    def optopen(outfile):
        if outfile is None:
            return sys.stdout
        else:
            return open(outfile, 'w+')

    with open(infile, 'rb') as inf:
        with optopen(outfile) as outf:
            delim = ''
            try:
                pos = 1
                for b in inf.read():
                    # Python 3 reads binary as int. Convert to string. 
                    byte = chr(b) if type(b) is int else b
                    outf.write(delim)
                    outf.write(encoder(byte))
                    delim = ' '
                    pos += 1
                outf.write('\n')
            except ValueError:
                sys.stderr.write('Can\'t encode data value {} at position {} in "%s"\n'.format(ord(b), pos, infile))
                sys.exit(1)
                
encoders = {'alpha': encode_alpha, 'ascii': encode_ascii, 'binary': encode_binary}

if __name__ == "__main__":
    args = parseargs()
    encode(encoders[args.coding], args.input, args.output)
        
