from fastapi import FastAPI, Request, BackgroundTasks
from fastapi.templating import Jinja2Templates
from starlette.middleware.cors import CORSMiddleware
from apscheduler.schedulers.background import BackgroundScheduler
from datetime import date, timedelta
import os
import atexit
from crawler import (asyncDongaSportsNewsCrawler,
                     asyncDongaHealthNewsCrawler,
                     asyncChosunSportsNewsCrawler,
                     asyncChosunHealthNewsCrawler,
                     asyncJoongangSportsNewsCrawler,
                     asyncJoongangHealthNewsCrawler,
                     asyncNaverSportsNewsCrawler,
                     asyncNaverHealthNewsCrawler)

app = FastAPI()

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

templates = Jinja2Templates(directory="templates")


@app.get("/")
def index(request: Request):
    return templates.TemplateResponse("index.html", {"request": request})


@app.get("/api/sports")
def sports_crawler(request: Request):
    return templates.TemplateResponse("sports.html", {"request": request})


@app.post("/api/sports")
async def sports_crawler(background_tasks: BackgroundTasks):
    await start_sports_crawler(background_tasks)


@app.get("/api/health")
def health_crawler(request: Request):
    return templates.TemplateResponse("health.html", {"request": request})


@app.post("/api/health")
async def health_crawler(background_tasks: BackgroundTasks):
    await start_health_crawler(background_tasks)


async def start_sports_crawler(background_tasks: BackgroundTasks):
    background_tasks.add_task(asyncChosunSportsNewsCrawler.start)
    background_tasks.add_task(asyncDongaSportsNewsCrawler.start)
    background_tasks.add_task(asyncJoongangSportsNewsCrawler.start)
    background_tasks.add_task(asyncNaverSportsNewsCrawler.start)


async def start_health_crawler(background_tasks: BackgroundTasks):
    background_tasks.add_task(asyncNaverHealthNewsCrawler.start)
    background_tasks.add_task(asyncJoongangHealthNewsCrawler.start)
    background_tasks.add_task(asyncChosunHealthNewsCrawler.start)
    background_tasks.add_task(asyncDongaHealthNewsCrawler.start)


def delete_old_files(): # 일주일 지난 크롤링 파일 삭제
    output_dir_list = ["output/sports/", "output/health/"]
    today = date.today()
    formatted_date = str(today - timedelta(days=7)).replace("-", "")
    for output_dir in output_dir_list:
        for file_name in os.listdir(output_dir):
            if formatted_date in file_name:
                file_path = os.path.join(output_dir, file_name)
                os.remove(file_path)


scheduler = BackgroundScheduler(timezone='Asia/Seoul')
scheduler.add_job(start_sports_crawler, 'cron', hour=11, minute=50)
scheduler.add_job(start_health_crawler, 'cron', hour=11, minute=50)
scheduler.add_job(delete_old_files, 'cron', day_of_week='sun', hour=0, minute=0)
scheduler.start()


@atexit.register
def shutdown_scheduler():
    scheduler.shutdown()
