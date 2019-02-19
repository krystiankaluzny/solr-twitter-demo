# solr-twitter-demo
Apache Solr vs RDBMS - searching text in tweets

Start app and open http://localhost:8081/

Apche Sorl store tweets core index on file system,
so before you restart the application delete target/classes/solr/tweets/data.

To search in RDBMS was used simple SQL LIKE statement.
So if you try to find *abc* phrase, then *abcdef* may be on list of results.

On the other hand Apache Solr tweets core uses StandardTokenizer to index texts,
so only whole words can be found.
And if you try to find *abc*, then you can get words like *abc*, *ABC*, *aBc*, but not *abcdef*

This leading to conclusion that searching something in RDBMS and Apache Solr with current configuration, may give you different results.
