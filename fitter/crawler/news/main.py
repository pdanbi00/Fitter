from fastapi import FastAPI, Request
from fastapi.templating import Jinja2Templates
from starlette.middleware.cors import CORSMiddleware
from crawler import (asyncDongaCrawler,
                     asyncChosunNewsCrawler,
                     asyncNaverNewsGeneralCrawler,
                     asyncJoongangSportsNewsCrawler,
                     asyncNaverNewsHealthCrawler)

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

@app.get("/api/chosun")
def chosun_crawler(request: Request):
    return templates.TemplateResponse("chosun.html", {"request": request})


@app.get("/api/donga")
def donga_crawler(request: Request):
    return templates.TemplateResponse("donga.html", {"request": request})


@app.get("/api/joongang")
def joongang_crawler(request: Request):
    return templates.TemplateResponse("joongang.html", {"request": request})


@app.get("/api/naver")
def naver_crawler(request: Request):
    return templates.TemplateResponse("naver.html", {"request": request})

@app.get("/api/naver-health")
def naver_health_crawler(request: Request):
    return templates.TemplateResponse("naver-health.html", {"request": request})


@app.post("/api/chosun")
def chosun_crawler():
    return asyncChosunNewsCrawler.start()

@app.post("/api/donga")
def donga_crawler():
    return asyncDongaCrawler.start()


@app.post("/api/naver")
def naver_crawler():
    return asyncNaverNewsGeneralCrawler.start()

@app.post("/api/naver-health")
def naver_health_crawler():
    return asyncNaverNewsHealthCrawler.start()


@app.post("/api/joongang")
def joongang_crawler():
    return asyncJoongangSportsNewsCrawler.start()
