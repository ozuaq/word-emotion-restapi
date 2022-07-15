# Word emotion REST API
与えられたワードでツイート検索をして、取得したツイートに含まれるネガティブ・ポジティブ・ニュートラルな単語を抽出して、カウントします。
<br>
<br>
これによって、そのワードが関連する事に対して、一般の人がネガティブな感情を抱いているか・ポジティブな感情を抱いているかの参考になります。

## Getting Strated (UNIX-likeなプラットフォーム)
以下、このmainリポジトリをクローンし、プロジェクトルートディレクトリでの作業を想定しています。
<br>
<br>
このアプリは[Sudachi](https://github.com/WorksApplications/Sudachi)(日本語形態素解析器)を用いるので、それに必要な辞書([SudachiDict](http://sudachi.s3-website-ap-northeast-1.amazonaws.com/sudachidict/))のセットアップをします。
<br>
<br>
zipファイルをダウンロードします。
```
$ curl -OL http://sudachi.s3-website-ap-northeast-1.amazonaws.com/sudachidict/sudachi-dictionary-latest-full.zip
```
zipファイルを解凍します。
```
$ unzip sudachi-dictionary-latest-full.zip
```
解凍したディレクトリ内のsystem_full.dicをsystem_core.dicにファイル名を変えてプロジェクトルートディレクトリに配置します。
<br>
<br>
java、またはmavenがセットアップされていなかったら、[SDKMAN!](https://sdkman.io/)などでセットアップします。
<br>
<br>
実行ファイル(jar)を生成します。
```
$ mvn install
```
実行ファイルをプロジェクトルートディレクトリにコピーします。
```
$ cp target/myapp-0.0.1-SNAPSHOT.jar ./myapp.jar
```
アプリを起動します。
```
$ java -jar myapp.jar --server.port=<ポート番号>
```
例）
```
java -jar myapp.jar --server.port=8888
```
## API利用方法
リクエスト方法
```
$ curl -X POST -H "Content-Type:application/json" -d "{\"word\":\"<word>\", \"max_results\":\"<max_results>\", \"max_queries\": <max_queries>, \"start_time\":\"<start_time>\", \"end_time\":\"<end_time>\"}" http://localhost:<port>/emotion
```
※Twitter APIとの対応：
<br>
max_resultsは一回のクエリで取得する最大のツイート件数(10 ~ 100)
<br>
max_queriesは最大のクエリ回数(1 ~ 180)
<br>
[Rate limits](https://developer.twitter.com/en/docs/twitter-api/rate-limits)があります。
<br>
start_time, end_timeは直近一週間で指定します。
<br>
<br>
※Twitter APIから利用してるもの：
<br>
[Search Tweets](https://developer.twitter.com/en/docs/twitter-api/tweets/search/api-reference/get-tweets-search-recent)
<br>
<br>
リクエスト例）
<br>
word：就活
<br>
max_results:100
<br>
max_queries:50
<br>
start_time:2022-07-14T00:00:00.000+09:00
<br>
end_time:2022-07-15T00:00:00.000+09:00
<br>
```
$ curl -X POST -H "Content-Type:application/json" -d "{\"word\":\"就活\", \"max_results\":\"100\", \"max_queries\": 50, \"start_time\":\"2022-07-14T00:00:00.000+09:00\", \"end_time\":\"2022-07-15T00:00:00.000+09:00\"}" http://localhost:8888/emotion
```
## 使わせていただいたもの
- 乾・鈴木研究室 [日本語評価極性辞書](https://www.cl.ecei.tohoku.ac.jp/Open_Resources-Japanese_Sentiment_Polarity_Dictionary.html)
   
    - 日本語評価極性辞書（名詞編）ver.1.0（2008年12月版）[pn.csv.m3.120408.trim](https://www.cl.ecei.tohoku.ac.jp/resources/sent_lex/pn.csv.m3.120408.trim)
- ワークス徳島人工知能NLP研究所[リソース](https://worksapplications.github.io/Sudachi/)
    - [Sudachi](https://github.com/WorksApplications/Sudachi):　日本語形態素解析器
    - [SudachiDict](https://github.com/WorksApplications/SudachiDict): 日本語形態素解析辞書
- [Twitter API](https://developer.twitter.com/en)
