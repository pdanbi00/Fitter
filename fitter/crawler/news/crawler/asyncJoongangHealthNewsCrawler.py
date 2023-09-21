import requests
from datetime import date
from bs4 import BeautifulSoup
import csv
import concurrent.futures
from datetime import datetime
import time

headers = {
    "User-Agent": "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.157 Safari/537.36"
}

def get_news():
    print("[중앙] 뉴스 목록 가져오는 중...")
    today = date.today()
    formatted_date = str(today).replace("-", "")
    formatted_date = int(formatted_date) - 1
    page = 1
    news_list = []
    flag = True
    while flag:
        url = f"https://www.joongang.co.kr/lifestyle/health?page={page}"
        rq = requests.get(url, headers=headers)
        soup = BeautifulSoup(rq.text, "html.parser")
        selectedList = soup.select("#story_list li")
        for temp in selectedList:
            newsDate = temp.select_one(".date").text
            newsDate = datetime.strptime(newsDate, "%Y.%m.%d %H:%M")
            newsDate = int(newsDate.strftime("%Y%m%d"))

            if newsDate > formatted_date:
                continue
            if formatted_date > newsDate:
                return news_list

            # title = title.replace("\n", "").replace("\t", "").replace("\r", "").lstrip().rstrip()
            news_list.append(
                {
                    "url": temp.select_one(".headline a")["href"],
                    "title": temp.select_one(".headline a").text,

                }
            )
        page += 1


def get_content(news):
    rq = requests.get(news["url"], headers=headers)
    soup = BeautifulSoup(rq.text, "html.parser")

    newsContent = soup.select_one("#article_body")

    for element in newsContent.select(".caption"):
        element.extract()

    newsContent = newsContent.select("p")

    content = ""

    for temp in newsContent:
        content += temp.text

    content = content.replace("\n", "").replace("\t", "").lstrip().rstrip()
    news["content"] = content


def save_to_csv(news_list):
    print("[중앙] csv 변환 중...")
    today = date.today()
    formatted_date = str(today).replace("-", "")
    formatted_date = int(formatted_date) - 1
    output_file_name = f"output/health/JoongangHealthNews{formatted_date}.csv"
    with open(output_file_name, "w", encoding="utf-8") as output_file:
        csvwriter = csv.writer(output_file, delimiter=";")
        csvwriter.writerow(news_list[0].keys())
        for i in news_list:
            csvwriter.writerow(i.values())


def start():
    start_time = time.time()
    news_list = get_news()
    if not news_list:
        print('---------------중앙 건강 뉴스 기사가 없습니다.--------------')
        return
    print("[중앙] 본문 가져오는 중...")
    with concurrent.futures.ThreadPoolExecutor() as executor:
        executor.map(get_content, news_list)
    save_to_csv(news_list)
    end_time = time.time()
    print("[중앙] 걸린시간 :", end_time - start_time)
    print("[중앙] 가져온 기사 :", len(news_list))
    print('---------------중앙 건강 뉴스 완료---------------')