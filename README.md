# PayaraMicroRestServer

JAX-RS+CDI+JPAで作ったRESTサービスを、Payara Microで動かします。

## 使用技術
- JDK 1.8.0_60
- JAX-RS 2.0
- CDI 1.2
- JPA 2.1


## 開発環境
- NetBeans 8.0.2
- Payara Micro 4.1.153
- JavaDB
- OS X El Capitan


## サンプルデータベース
Payara Micro内包のJavaDBを使っています。
JPAのDBスキーマ生成機能を使っていますので、アプリ起動時にDBにレコードが自動追加されます。
テーブルは、employee(社員)とdepartment(部署)の2つです。

- employee
| id(PK) | name | joined_date | dept_id(FK) |
| --- | --- | --- | --- |
| 1 | Yumi Wakatsuki | 2015-04-01 | 1 |
| 2 | Mai Fukagawa | 2015-04-01 | 1 |
| 3 | Erika Ikuta | 2015-04-01 | 2 |
| 4 | Reika Sakurai | 2015-04-01 | 2 |
| 5 | Nanase Nishino | 2015-04-01 | 2 |

- department
| dept_id(PK) | name |
| --- | --- |
| 1 | Sales |
| 2 | Development |


## 起動方法

起動する方法は2種類あります。

+ main()メソッドから起動
IDEから、このプロジェクトのcom.example.Main.main()を起動してください。（NetBeans及びIDEAで確認済み）

+ コンソールからjavaコマンドで起動
payara-micro-4.1.153.jarは、別途ダウンロードし、任意のディレクトリに置いてください。
```
java -jar <dir-name>/payara-micro-4.1.153.jar --deploy <project-root>/lib/rest.war
```

## WebAPI仕様

### 正常系
#### リクエスト例
GET /rest/api/employees?name=m HTTP/1.1

#### レスポンス例
HTTP/1.1 200 OK
Content-Type: application/json;charset=UTF-8
```
[ {
  "name" : "Yumi Wakatsuki",
  "emp_id" : 1,
  "joined_date" : 1427814000000,
  "department" : {
    "name" : "Sales",
    "dept_id" : 1
  }
}, {
  "name" : "Mai Fukagawa",
  "emp_id" : 2,
  "joined_date" : 1427814000000,
  "department" : {
    "name" : "Sales",
    "dept_id" : 1
  }
} ]
```

### 異常系(クエリパラメータnameが空文字、半角スペース、無しの場合)

#### リクエスト例
GET /rest/api/employees?name= HTTP/1.1
または
GET /rest/api/employees?name=   HTTP/1.1
または
GET /rest/api/employees? HTTP/1.1
または
GET /rest/api/employees HTTP/1.1

### 異常系(検索結果が0件の場合)

#### リクエスト例
GET /rest/api/employees?name=X HTTP/1.1

#### レスポンス例
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8
```
{
  "messages" : [ "該当する社員は存在しません" ],
  "exception_class" : "NotFoundException"
}
```
注意：エラーレスポンスにJavaの例外クラス名を含めることは、本来は良くありません。あくまでサンプルとして捉えてください。


#### レスポンス例
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8
```
{
  "messages" : [ "社員名は必ず指定してください", "社員名はアルファベットで入力してください" ],
  "exception_class" : "ConstraintViolationException"
}
```
注意：エラーレスポンスにJavaの例外クラス名を含めることは、本来は良くありません。


### 異常系(クエリパラメータnameにアルファベット以外が指定されていた場合)

#### リクエスト例
GET /rest/api/employees?name=1 HTTP/1.1

#### レスポンス例
HTTP/1.1 400 Bad Request
Content-Type: application/json;charset=UTF-8
```
{
  "messages" : [ "社員名はアルファベットで入力してください" ],
  "exception_class" : "ConstraintViolationException"
}
```
注意：エラーレスポンスにJavaの例外クラス名を含めることは(ry)


### 異常系(クエリパラメータnameに"z"または"Z"が指定されていた場合)

#### リクエスト例
GET /rest/api/employees?name=z HTTP/1.1

#### レスポンス例
HTTP/1.1 500 Internal Server Error
Content-Type: application/json;charset=UTF-8
```
{
  "messages" : [ "zは入力できません" ],
  "exception_class" : "Exception"
}
```
注意：エラーレスポンスにJavaの例外クラス名(ry)
注意：「ZだったらException」というのも、あくまで例外処理のサンプルですので・・・。
