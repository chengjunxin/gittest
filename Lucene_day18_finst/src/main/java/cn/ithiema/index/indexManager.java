package cn.ithiema.index;

import cn.ithiema.dao.BookDao;
import cn.ithiema.dao.impl.BookDaoimpl;
import cn.ithiema.po.Book;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.junit.Test;
import org.wltea.analyzer.lucene.IKAnalyzer;

import javax.management.Query;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class indexManager {
    private static final String INDEX_PATH = "C:\\Users\\chenjunxin\\Downloads\\index\\";

    @Test
    public void addManage() throws Exception {
        //    1.	采集数据
        BookDao bookDao = new BookDaoimpl();
        List<Book> bookList = bookDao.findAllBooks();
        //    2.	建立文档对象（Document）
        List<Document> docList = new ArrayList<Document>();
        for (Book book : bookList) {
//            创建文档对象
            Document doc = new Document();
            doc.add(new StringField("bookId", book.getId() + "", Field.Store.YES));
            doc.add(new TextField("bookName", book.getBookname(), Field.Store.YES));
            doc.add(new DoubleField("bookPrice", book.getPrice(), Field.Store.YES));
            doc.add(new StringField("bookPic", book.getPic(), Field.Store.YES));
            doc.add(new TextField("bookDesc", book.getBookdesc(), Field.Store.YES));
            docList.add(doc);
        }



        //    3.	建立分析器（分词器）对象(Analyzer)，用于分词
        Analyzer analyzer = new IKAnalyzer();
        //    4.	建立索引库配置对象（IndexWriterConfig），配置索引库
        IndexWriterConfig iwc = new IndexWriterConfig(Version.LUCENE_4_10_3,analyzer);
        //    5.	建立索引库目录对象（Directory），指定索引库的位置
        File file = new File("C:\\Users\\chenjunxin\\Downloads\\index\\");
        Directory directory = FSDirectory.open(file);
        //    6.	建立索引库操作对象（IndexWriter），操作索引库
        IndexWriter indexWriter = new IndexWriter(directory,iwc);
        //    7.	使用IndexWriter，把文档对象写入索引库



        for (Document doc:docList) {
            indexWriter.addDocument(doc);
            
        }
        //    8.	释放资源
        indexWriter.close();

    }

    @Test
    public void readindex() throws Exception {
//        1.	建立分析器对象（Analyzer），用于分词
        Analyzer analyzer = new IKAnalyzer();
//        2.	建立查询对象（Query）
//        建立查询解析器对象
        QueryParser parser = new QueryParser("bookName",analyzer);
//        使用查询解析器对象 实例化Query对象
    org.apache.lucene.search.Query query = parser.parse("bookName:Lucene");


//        3.	建立索引库目录对象（Directory），指定索引库的位置
        Directory directory = FSDirectory.open(new File(INDEX_PATH));
//        4.	建立索引读取对象（IndexReader），把索引数据读取内存中
        IndexReader reader = DirectoryReader.open(directory);
//        5.	建立索引搜索对象（IndexSearcher），执行搜索，返回搜索的结果集（TopDocs）
        IndexSearcher searcher = new IndexSearcher(reader);

        TopDocs docs = searcher.search(query, 10);
//        6.	处理结果集
        System.out.println("实际搜索到的数量 "+docs.totalHits);
        ScoreDoc[] scoreDocs = docs.scoreDocs;
        for (ScoreDoc sd:scoreDocs) {
            System.out.println("-------------------------------------------");
            int docId = sd.doc;
            float score = sd.score;
            System.out.println("当前的id"+docId+"分词 "+ score);
            Document document = searcher.doc(docId);
            System.out.println("图示id"+document.get("bookId"));
            System.out.println("图示名称"+document.get("bookName"));
            System.out.println("图示价格"+document.get("bookPrice"));
            System.out.println("图示图片"+document.get("bookPic"));
            System.out.println("图示详情"+document.get("bookDesc"));
        }

//        7.	释放资源
        reader.close();


    }
}
