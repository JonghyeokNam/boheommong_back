import random
import csv
import os

#############################
# 1) 사용자 더미 데이터 (users.csv)
#############################

roles = ["USER", "ADMIN"]
family_names = ["김", "이", "박", "최", "정", "강", "조", "윤", "장", "오", "한", "신", "서", "유", "권", "황", "안", "송", "전", "홍"]
given_names = ["민수","영희","도윤","서연","지민","하윤","서현","지원","현우","유정","가영","상민","동혁","다은","민기","화경","정은","혜정"]

def generate_users(num=500):
    """
    user_id (1..num)
    loginEmail, userEmail
    name, role
    """
    data = []
    for user_id in range(1, num+1):
        f_name = random.choice(family_names)
        g_name = random.choice(given_names)
        user_name = f_name + g_name

        login_email = f"kakao_{random.randint(1000,9999)}@kakao.com"
        user_email  = f"user{random.randint(1000,9999)}@gmail.com"
        role        = random.choice(roles)

        data.append([
            user_id,
            login_email,
            user_email,
            user_name,
            role
        ])
    return data

def save_users_csv(users_data, filename="users.csv"):
    headers = ["user_id", "loginEmail", "userEmail", "name", "role"]
    with open(filename, "w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(headers)
        writer.writerows(users_data)


#############################
# 2) 보험상품 더미 (insurance_products.csv)
#############################

# 회사별 상품명 (카테고리 정보 없음)
base_products = {
    "삼성화재": [
        "삼성화재 뉴 암플랜",
        "삼성화재 건강보험 통합",
        "삼성화재 종신보험 스마트",
        "삼성화재 운전자패키지 Plus",
        "삼성화재 치아보험 웰라이프",
        "삼성화재 The건강한 플랜",
        "삼성화재 다이렉트 실손건강",
        "삼성화재 행복한 보장 플랜",
        "삼성화재 수술입원 통합보장",
        "삼성화재 무배당 내아이 케어",
        "삼성화재 신생아 희망케어",
        "삼성화재 치매간병 안심플랜",
        "삼성화재 스마트 운전자케어",
        "삼성화재 가정화재 종합보장",
        "삼성화재 알뜰한 치아케어"
    ],
    "DB손해보험": [
        "DB손해보험 참좋은 암보험",
        "DB손해보험 운전자보험 프로미",
        "DB손해보험 종합건강수술비",
        "DB손해보험 아이러브 건강보험",
        "DB손해보험 웰케어 실손보험",
        "DB손해보험 노블레스 종신보험",
        "DB손해보험 수술입원비 플러스",
        "DB손해보험 치아든든케어",
        "DB손해보험 고혈압당뇨 케어플랜",
        "DB손해보험 하우스안심 화재보험",
        "DB손해보험 다이렉트 운전자보험",
        "DB손해보험 암플러스 3대질환",
        "DB손해보험 치매간병 프리미엄",
        "DB손해보험 신생아미숙아 보장",
        "DB손해보험 가정종합화재 특별약정"
    ],
    "KB손해보험": [
        "KB 암보험 플래티넘",
        "KB 치아보험 스마일",
        "KB 운전자보험 스마트운전",
        "KB 종신보험 행복누리",
        "KB 치매간병보험 안심케어",
        "KB 종합건강보험 The Care",
        "KB 실손의료비 표준플랜",
        "KB 신생아케어 안심보장",
        "KB 주택화재 종합프로",
        "KB 운전자라이프 파워",
        "KB 무배당 암케어슈퍼",
        "KB 다이렉트 입원수술 종합",
        "KB 골든라이프 종신플랜",
        "KB 치아안심 파워케어",
        "KB 뇌심장질환 종합보장"
    ],
    "현대해상": [
        "현대해상 굿앤굿종합보험",
        "현대해상 하이카 운전자보험",
        "현대해상 신실손의료비보험",
        "현대해상 암스트롱 암보험",
        "현대해상 The 간편건강보험",
        "현대해상 수술입원 안심케어",
        "현대해상 종신플랜 브라이트",
        "현대해상 치아케어 치유플러스",
        "현대해상 주택화재 맘편한",
        "현대해상 신생아 고위험케어",
        "현대해상 운전자 패밀리안심",
        "현대해상 치매보장 뉴케어",
        "현대해상 다이렉트 건강입원비",
        "현대해상 노후실손 간편보장",
        "현대해상 수술비 더든든"
    ],
    "메리츠화재": [
        "메리츠 알파Plus 보장보험",
        "메리츠 운전자보험 안전플랜",
        "메리츠 치아보험 내돈제로",
        "메리츠 종신보험 든든케어",
        "메리츠 실손의료비 New",
        "메리츠 암보험 파워케어",
        "메리츠 어린이건강 행복지킴이",
        "메리츠 수술입원비 블루케어",
        "메리츠 암수술 특약플러스",
        "메리츠 치아사랑 메디케어",
        "메리츠 주택화재 가정지킴이",
        "메리츠 치매든든 종합간병",
        "메리츠 단독운전자 안심보장",
        "메리츠 유니버셜 종신플랜",
        "메리츠 실버케어 건강입원"
    ],
    "한화손해보험": [
        "한화손해 저축성 종신보험",
        "한화손해 스마트치아보험",
        "한화손해 암보험 3대질환케어",
        "한화손해 안심운전자 종합",
        "한화손해 주택화재 AllSafe",
        "한화손해 실손의료비 표준형",
        "한화손해 치매케어 파워플랜",
        "한화손해 어린이 New케어",
        "한화손해 하이종신 든든보장",
        "한화손해 암수술비 업그레이드",
        "한화손해 수술입원 The건강",
        "한화손해 신생아스페셜 보장",
        "한화손해 치아굿케어 A+",
        "한화손해 운전자배상 종합안심",
        "한화손해 노후치매 프리미엄"
    ],
    "롯데손해보험": [
        "롯데 가온종합보험",
        "롯데 치아건강보험",
        "롯데 암보험 바로OK",
        "롯데 운전자보험 드림카",
        "롯데 수술입원 종합케어",
        "롯데 종신보험 행복드림",
        "롯데 신생아 안심플랜",
        "롯데 치매보장 New해피라이프",
        "롯데 화재플러스 내집안심",
        "롯데 실손의료비표준 A형",
        "롯데 어린이건강 Smart",
        "롯데 운전자 무배당 프로",
        "롯데 치아실속케어 B형",
        "롯데 암입원프리미엄 플랜",
        "롯데 노후실손 든든케어"
    ],
    "흥국화재": [
        "흥국화재 착한운전자보험",
        "흥국화재 행복을다주는암보험",
        "흥국화재 간병치매보험 안심",
        "흥국화재 주택화재 럭키홈",
        "흥국화재 종신플러스 NewLife",
        "흥국화재 수술입원 Protect",
        "흥국화재 어린이미래 파워케어",
        "흥국화재 치아클리닉 굿케어",
        "흥국화재 운전자안전 all-in",
        "흥국화재 신생아 출산플랜",
        "흥국화재 암간병 든든플러스",
        "흥국화재 실손건강 표준형",
        "흥국화재 치매간병 해피라이프",
        "흥국화재 노후장기 케어플랜",
        "흥국화재 유니버셜 종신 New"
    ]
}

# 만약 회사별 상품 수가 15개 미만이면, 100개까지 임의 생성
for company, products_list in base_products.items():
    if len(products_list) < 100:
        start_len = len(products_list)+1
        for i in range(start_len, 101):
            products_list.append(f"{company} 추가상품{i}")


#############################
# 2-1) 카테고리(Enum)별 보장내용 map
#############################

base_coverage_details = {
    "CANCER": [
        "주요암(유방암, 폐암) 보장. 항암주사비 지원.",
        "유전성 암 스크리닝 할인, 가족력시 보험료 감면.",
        "재진단암(2차) 보장, 항암약물치료비 일부.",
        "말기암 간병특약, 호스피스 비용 지원.",
        "초기암부터 고액암까지 단계별 혜택.",
        "암 사망 시 유족연금 지급형 가능.",
        "표적항암약물, 면역치료 보장 확대.",
        "소액암, 갑상선암 전립선암 특화담보.",
        "5년 무사고 보험료 환급 옵션.",
        "정기검진 결과 고위험군 할인이벤트."
    ],
    "SURGERY": [
        "수술비·입원비 포괄 보장. 로봇수술 특약.",
        "복강경, 관절, 디스크 등 정형외과 수술 강화.",
        "응급수술 시 헬기이송, 보호자 식대비 지원.",
        "수술 후 재활치료, 도수치료 담보.",
        "무사고 시 수술비 환급 옵션.",
        "특정질환(암·뇌·심장) 수술 2배 지급.",
        "심장수술·뇌수술 고액 특약 별도.",
        "치과수술(악교정, 임플란트) 일부 커버.",
        "미용 목적 성형 제외, 재건술은 일부 조건.",
        "입원일당×(수술난이도계수)로 차등 보상."
    ],
    "LIFE": [
        "사망 시 유족보험금 평생 지급. 중도 해지환급 일부.",
        "정액사망보장 + 뇌·심장질환 특약 가능.",
        "납입기간 단축(10/15년), 고액 해지환급 설계.",
        "배우자 연결 종신, 자녀 양육비 연금화 가능.",
        "암·뇌졸중 사망 시 2배 보상 특약.",
        "기업 오너용 상속세 대비 플랜 가능.",
        "정년도래형: 60세 납 끝, 평생 보장.",
        "생활자금선지급형: 중증질환 시 일부 지급.",
        "MCI(경증치매) 선지급, 간병비로 활용.",
        "변액 종신, 투자수익 연동 해지환급 가능."
    ],
    "DRIVER": [
        "교통사고 처리지원금, 변호사비, 벌금 지원.",
        "상해 입원비, 후유장해. 자전거·오토바이 특약.",
        "음주운전 벌금 일부 제외(도덕적 해이 방지).",
        "무보험차 상해, 긴급견인 무제한.",
        "면허정지/취소 위로금, 벌금 한도 3천만원.",
        "법률비용지원: 변호사 선임비, 합의금.",
        "배달 라이더 전용, 이륜차 상해 확장.",
        "가족 운전 중 사고시 동일 보장.",
        "면허 재취득 비용 특약, 심리치료 연계.",
        "장기무사고 환급, 블랙박스 설치 할인."
    ],
    "FIRE": [
        "화재, 폭발, 누출 피해. 가재도구 망실까지 보장.",
        "자연재해(태풍, 홍수) 임시거주비 6개월.",
        "임차인 배상책임 별도. 전세·월세 모두 안심.",
        "화상 치료비, 인명 피해 위로금 커버.",
        "보일러, 수도 파열사고 확대. 도난 특약 가능.",
        "고층건물 화재시 고가사다리차 비용 일부.",
        "노후주택(건축30년↑) 특별심사, 일부 할증.",
        "반려동물 대피 시 임시보호소 비용.",
        "지진담보 특약, 건물 균열·붕괴 대비.",
        "무사고3년 환급, 장기유지 보너스."
    ],
    "DENTAL": [
        "스케일링, 충치, 신경치료 보장. 임플란트 특약.",
        "치아보철, 교정 일부 지원. 어린이 교정은 별도.",
        "치주질환, 발치, 근관치료 단계별 정액.",
        "어금니, 브릿지, 골이식술 일부 비용.",
        "스케일링 연2회, 협회 등록병원 할인.",
        "유아·어린이 충치치료, 실란트 시술 보장.",
        "임신 중 치아건강 특별관리, 산모 치주질환.",
        "치아염증 재발 시 2차 치료비 커버.",
        "보철 파손 재제작 비용 커버. 지진·사고 등.",
        "임플란트 후 통증관리 약제비 일부."
    ],
    "DEMENTIA": [
        "경도치매~중증치매 단계별 간병비. 장기요양등급별 확대.",
        "치매 간병인 24시간 특약. 배우자 동시 발생 시 2배.",
        "알츠하이머, 혈관성 치매 집중 담보.",
        "치매 진단 시 보험료 납입면제. 보호자 심리상담.",
        "가족 중 치매 이력 있으면 가입 할인.",
        "중증치매 간병병실 차액 지원. 호스피스 비용.",
        "인지재활치료(작업·언어) 비용 특약.",
        "치매 환자 실종보험금, 배회감지기 지원.",
        "장기요양1~5등급 월 간병비, 최대36개월.",
        "치매 사망 시 위로금, 유족연금형 선택 가능."
    ],
    "NEWBORN": [
        "출생 직후 미숙아·선천질환 보장, 인큐베이터 입원비.",
        "산모 위험담보, 제왕절개 수술비 등.",
        "저체중아 집중케어, 희귀질환 확대 담보.",
        "1세 이전 상해·화상·골절 보장, 응급실 포함.",
        "쌍둥이 출산 시 추가 할인 특약.",
        "아토피, 알레르기성 비염 초기치료.",
        "영유아 검진 비용 전액 지원(정부 외 추가).",
        "신생아 황달, 호흡곤란증후군 집중보장.",
        "산후조리원 이용비, 일부 지정기관 할인.",
        "태아 때 초음파 검사비 환급, 산전검사 쿠폰."
    ],
    "HEALTHCARE": [
        "표준 실손의료비, 급여·비급여 통원/입원 보장.",
        "본인부담금(약국·처방조제료, MRI 등) 지원.",
        "도수치료, 증식치료 등 비급여 일부 커버.",
        "상급병실 차액, 특진료 일부 담보.",
        "연간 보장한도 내 무제한 청구 가능.",
        "중증질환 고액치료비 한도 확대.",
        "미용 목적 시술 제외, 치료목적 성형은 일부.",
        "대학병원 1인실 차액 정액지원.",
        "약국 본인부담금, 처방조제료 연간 n회 제한.",
        "4세대 실손구조, 과다 이용 시 할증."
    ],
    "CHILD": [
        "어린이(0~15세) 질병·상해 종합보험. 골절, 화상 등.",
        "소아암(백혈병, 뇌종양) 특화담보. 재진단도 일부 보장.",
        "어린이 치아치료, 실란트 시술 50%이상 보장.",
        "학교·학원 사고, 통원치료비, 응급실 비용.",
        "정신건강상담 특약(ADHD, 틱장애).",
        "만기 후 성인용 전환 가능, 저해지 설계.",
        "피부질환(아토피), 알레르기성 비염도 보강.",
        "입원 간병인 쿠폰, 부모 동반 간이침대비.",
        "어린이 뇌전증 등 희귀질환 지원.",
        "영양상담·성장검진 할인 연계."
    ],
    "PET": [
        "반려동물(개·고양이) 의료비 보장. 수술, 입원, 통원비.",
        "중성화수술비, 예방접종비 일부 환급. 광견병 백신 등.",
        "산책 중 타인 배상책임 담보, 한도 설정.",
        "노령동물(10세 이상) 가입할증, 건강검진 권장.",
        "암진단 시 고액치료비 일부 보전. 항암제·면역치료 지원.",
        "슬관절 탈구, 피부질환 등 특정품종 특약.",
        "펫장례 특약(화장, 봉안), 펫이별관리 서비스.",
        "반려견 등록비, 유실견 찾기 쿠폰 연계.",
        "수술 후 재활(수중런닝머신) 일부 보상.",
        "가입 시 펫사료 할인쿠폰, 정기검진 안내."
    ],
    "NURSING": [
        "간병보험: 장기요양1~5등급 등급별 간병비 월지급.",
        "요양병원 입원비, 간병인 고용비 일부 커버.",
        "퇴원 후 방문간호, 복지용구 대여비 지원.",
        "납입면제(중증질환) 제도, 가족간병 특약.",
        "치매·뇌졸중 등 노인성질환 간병비 연동.",
        "방문요양 쿠폰 연2회, 보호자 심리상담.",
        "시설입소 보증금 파손 위험 담보, 파산시 일부 환급.",
        "간편심사(3·2·1문) 노령층 가입 용이.",
        "재활치료비, 작업치료 옵션 별도.",
        "무심사형(보장금액 제한) 플랜 존재."
    ],
    "TRAVEL": [
        "해외여행 중 질병·상해, 항공기 지연, 수하물 파손 보장.",
        "여권 분실 시 재발급 비용, 긴급현금지원.",
        "스쿠버·스카이다이빙 등 레저특약(고위험 할증).",
        "코로나 확진 시 여행취소비용 일부 보상.",
        "해외입원, 현지 간호인 파견, 법률지원.",
        "항공편 결항, 숙박연장 비용, 일정 변경.",
        "배상책임(타인·시설 파손) 변호사비.",
        "24시간 핫라인, 통역서비스, 대사관 협조.",
        "스마트폰·노트북 분실·파손 한도.",
        "장기유학·워홀 전용 설계, 연장 가능."
    ],
    "ETC": [
        "골프보험, 낚시보험, 레저보험 등 이색 상품.",
        "반려식물보험, 악기파손보험, 특정이벤트 단기보험.",
        "기업 임직원 단체상해, 복지특약, 보증보험 등.",
        "축제·행사 배상책임, 인원안전 담보, 장비 파손.",
        "연예인·스턴트맨 고위험직군 전용. 산재사각 보완.",
        "예술품·명품가방 파손·도난 담보.",
        "농작물재해, 가축재해, 자연재해 연결 특약.",
        "특정 질병(에이즈, 희귀질환) 전용 플랜.",
        "우주여행 대비 고공비행보험(개발 중).",
        "가정종합+이색담보(드론, 자전거, 캠핑장비)."
    ]
}

# 필요 시 각 key에 최소 10개 이상의 문구가 되도록 확장
def extend_coverage(min_count=10):
    for k, arr in base_coverage_details.items():
        while len(arr) < min_count:
            arr.append(f"{k} 추가보장문구 {len(arr)+1}")
extend_coverage(min_count=10)

category_list = list(base_coverage_details.keys())  # Enum name list


def generate_insurance_products(num=500):
    """
    - product_id: 1..num
    - company_name: from base_products
    - product_name: from base_products[company]
    - product_type: from category_list (Enum name)
    - coverage_details: from base_coverage_details[product_type]
    - monthly_premium: random
    """
    data = []
    product_id_start = 1
    companies = list(base_products.keys())

    for _ in range(num):
        company = random.choice(companies)
        product_name = random.choice(base_products[company])

        product_type = random.choice(category_list)  # e.g. "CANCER","SURGERY",...
        coverage_list = base_coverage_details[product_type]
        coverage_text = random.choice(coverage_list)

        premium = random.randint(15000, 60000)

        data.append([
            product_id_start,
            company,
            product_name,
            product_type,        # Enum name
            coverage_text,
            premium
        ])
        product_id_start += 1

    return data

def save_insurance_products_csv(data, filename="insurance_products.csv"):
    headers = ["product_id","company_name","product_name","product_type","coverage_details","monthly_premium"]
    with open(filename, "w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(headers)
        writer.writerows(data)


#############################
# 3) 건강정보 (UserHealth) (user_health.csv)
#############################

job_type_list = [
    "OFFICE",
    "DELIVERY",
    "CONSTRUCTION",
    "SELF_EMPLOYED",  # 변경
    "STUDENT",
    "HOUSEWIFE",      # 변경
    "UNEMPLOYED"
]

def generate_user_health(num=500):
    """
    - health_id: 1..num
    - user_id  : 1..num (1:1)
    - age, gender("M"/"F"), height(cm), weight(kg), bmi(float)
    - bloodPressureLevel(1~5), bloodSugarLevel(1~5)
    - surgeryCount(0~5)
    - isSmoker("true"/"false"), isDrinker("true"/"false")
    - chronicDiseaseList(최대 2개 콤마)
    - jobType(OFFICE,DELIVERY,CONSTRUCTION,SELFEMP,STUDENT,HOMEMAKER,UNEMPLOYED)
    - hasChildren("true"/"false"), hasOwnHouse("true"/"false")
    - hasPet("true"/"false"), hasFamilyHistory("true"/"false")
    """
    data = []
    chronic_list = ["고혈압","당뇨","고지혈증","천식","관절염","뇌졸중","협심증","암","간염","심부전"]

    for health_id in range(1, num+1):
        user_id = health_id  # 1:1

        age = random.randint(1,85)
        gender = random.choice(["M","F"])
        height = random.randint(140,190)
        weight = random.randint(40,100)
        bmi_val = 0.0
        if height > 0:
            bmi_val = round(weight / ((height/100)**2), 1)

        bp_level = random.randint(1,5)
        bs_level = random.randint(1,5)
        scount = random.randint(0,5)

        smoker_flag  = "true" if random.random()<0.3 else "false"
        drinker_flag = "true" if random.random()<0.4 else "false"

        # 만성질환
        cdl = ""
        if random.random() < 0.3:
            pick_count = random.randint(1,2)
            pick = random.sample(chronic_list, pick_count)
            cdl = ",".join(pick)

        jt = random.choice(job_type_list)

        has_children       = "true" if random.random()<0.4 else "false"
        has_own_house      = "true" if random.random()<0.3 else "false"
        has_pet            = "true" if random.random()<0.25 else "false"
        has_family_history = "true" if random.random()<0.2 else "false"

        data.append([
            health_id,
            user_id,
            age,
            gender,
            height,
            weight,
            bmi_val,
            bp_level,
            bs_level,
            scount,
            smoker_flag,
            drinker_flag,
            cdl,
            jt,
            has_children,
            has_own_house,
            has_pet,
            has_family_history
        ])
    return data

def save_user_health_csv(data, filename="user_health.csv"):
    headers = [
        "health_id",
        "user_id",
        "age",
        "gender",
        "height",
        "weight",
        "bmi",
        "bloodPressureLevel",
        "bloodSugarLevel",
        "surgeryCount",
        "isSmoker",
        "isDrinker",
        "chronicDiseaseList",
        "jobType",
        "hasChildren",
        "hasOwnHouse",
        "hasPet",
        "hasFamilyHistory"
    ]
    with open(filename, "w", newline="", encoding="utf-8") as f:
        writer = csv.writer(f)
        writer.writerow(headers)
        writer.writerows(data)


#############################
# 메인 실행
#############################
if __name__ == "__main__":
    # 이미 CSV가 있으면 스킵
    if os.path.exists("users.csv") or os.path.exists("insurance_products.csv") or os.path.exists("user_health.csv"):
        print("[INFO] CSV 파일이 이미 존재하므로 생성 스킵!")
        exit(0)

    print("=== CSV 더미데이터 생성 시작 ===")

    # 1) 유저 500명 -> users.csv
    user_data = generate_users(num=500)
    save_users_csv(user_data, "users.csv")
    print("users.csv 생성 완료 (500건)")

    # 2) 보험상품 500개 -> insurance_products.csv
    #    카테고리는 Enum name (CANCER, SURGERY 등) 중 랜덤
    product_data = generate_insurance_products(num=500)
    save_insurance_products_csv(product_data, "insurance_products.csv")
    print("insurance_products.csv 생성 완료 (500건)")

    # 3) 건강정보 500개 -> user_health_info.csv
    #    health_id = user_id (1:1)
    health_data = generate_user_health(num=500)
    save_user_health_csv(health_data, "user_health.csv")
    print("user_health.csv 생성 완료 (500건)")

    print("=== CSV 파일 생성 끝 ===")
