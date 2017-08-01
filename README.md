# scala-packed

A \[early prototype of\] scala library for making memory-optimized data structures.

General idea:

* pack all members of case-class like objects to a single ByteArray buffer
* use a single shared buffer for all elements in a sequence
* supports String, Float, Double, Int, Long types
* supports Option[T], Map[K,V], Seq[T]
* can be extended for custom types


### hello world example

    // A simple case class to pack
    case class HelloPacked(i: Int, l: Long, s: String)
    
    // codecs for default scala types
    import io.findify.scalapacked.codec._
    // shapeless-based codec auto-deriver for case classes
    import io.findify.scalapacked.codec.generic._
    
    // a sequence of 1k objects
    val list = (0 to 1000).map(i => HelloPacked(i, i.toLong, i.toString)).to[PackedSeq]
    
    // use it as a typical scala collection
    list.filter(_.i % 10 == 0)
    
    
### Performance

RAM usage is 5x-10x lower compared to generic collections of case classes.

CPU usage depends on workload, but in general it is 3x-5x slower than scala native collections.

## Licence

The MIT License (MIT)

Copyright (c) 2017 Findify AB

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.