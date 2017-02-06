def xparser = new XmlSlurper()
def targetFile = new File("test.xml")

def gpathResult = xparser.parse(targetFile)

def book0 = gpathResult.value.books.book[0]
println "book0:" + book0.author + " id:" + book0.author.@id

def book = gpathResult.book
println "book:" + book.title + " available:" + book.@available + " id:" + book['@id']

