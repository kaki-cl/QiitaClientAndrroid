# LightQiita
これは私(kaki-cl)のAndroid開発学習のために作成した、非公式のQiitaクライアントアプリです。
近日中にGoogle Playにも公開します。

## 機能
- キーワード検索。
- Qiita記事のWebView表示。
- Drawerでの検索キーワード履歴の表示。(タッチすると選択したキーワードで検索が実行されます。)

### キーワード検索+Qiita記事のWebView表示
![lightqiita_2](https://user-images.githubusercontent.com/23579885/38480022-eea7aa5a-3bfd-11e8-8a91-f0fb50538280.gif)

### 検索キーワード履歴の表示
![lightqiita_2_drawer](https://user-images.githubusercontent.com/23579885/38480019-eae14584-3bfd-11e8-8eea-fb181d7e243c.gif)


## その他
- 一度検索したキーワードは1時間以内であれば、端末のSQLiteに保存された検索結果を表示します。

## 設計
MVVMにて実装を行いました。
Comming Soon

## Library
- RxJava 2.0.2
- Retrofit 2.4.0
- orma 4.2.3
- picasso 2.71828

## Author

[kaki-cl](https://github.com/kaki-cl)
