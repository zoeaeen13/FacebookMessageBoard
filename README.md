## 挑戰賽：實作留言板
這周接到後端 mentor 突然發布的挑戰賽，要求做出一個仿 Facebook 介面的留言板，功能及要求如下：
各 camp 拆散分組，和 Backend - Sarah、Web - XTim 及熟悉的夥伴 iOS - Polly 一組。

</br>

### 後端要求：
1. 請保留目前做好的留言板功能，另外開立 API 給前端串接，內容至少要包含
    - 會員登入系統 (registe / login / logout)，發表文章、回覆留言、按讚都需要登入後才可使用，未登入者，只可觀看文章及觀看留言(也不可按讚)
    - 留言板可以發表文章，需記錄 `發表時間`、`發表人`、`留言內容`、顯示 `按讚人數`
    - 可以對文章 `按讚`、`收回讚`
    - 點選 `按讚人數` 可觀看所有有按過讚的人
    - 可以對文章做回覆，需記錄`回覆時間`、`回覆人`、`回覆內容`
    - 可以對文章的回覆做回覆，需記錄`回覆時間`、`回覆人`、`回覆內容`
    - 所有的東西都要依時間排序，最新的留言在 `最上面`
2. 請使用任一工具撰寫 API 文件
3. 請找至少一位非 backend camp 來接 API，完成一樣的功能

### 前端要求：
1. Web 及 App 留言版版型一樣，如上圖。會員註冊、登入畫面不限
2. 請小心注意地做 commit
3. 請寫 README 並推到 github
    - README 需包含
        1. 安裝及使用步驟
        2. 使用畫面或操作方式

### 額外要求
1. 把 Web 端、後端的 code 都部署到同一台機器上
2. 使用任一個 ci/cd 工具，讓 Web 端、後端任一開發者 push 後可以自動佈署
3. 文章、留言時間顯示
    - 距離現在，小於一個小時內，顯示幾分鐘前
    - 距離現在，大於一個小時但小於一天內，顯示幾小時前
    - 距離現在，大於一天，顯示幾月幾號
</br>

---

## 實際 Demo： 留言板

### 登入註冊介面
* 註冊/登入
* 輸入帳號密碼
* 亦可透過最下方連結，直接進入留言板查看

![](https://i.imgur.com/UK9pIuj.png)

### 留言板
#### 介面
* 貼文內容、按讚數、留言數、留言內容、時間

#### 功能
* 發佈貼文、留言/評論
* 按讚、回覆留言
* 下方有離開按鈕

![](https://i.imgur.com/xmyJTHU.png)

* 可查看按讚者

![](https://i.imgur.com/GUNCEnF.png)


