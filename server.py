from flask import Flask, jsonify, Response
from selenium import webdriver
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.by import By
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from webdriver_manager.chrome import ChromeDriverManager
import traceback, time
import json

app = Flask(__name__)

@app.route("/credit-offers")
def get_credit_offers():
    try:

        chrome_options = Options()
        chrome_options.add_argument("--headless")  # или "--headless=new"
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--disable-dev-shm-usage")

        chrome_options.add_argument("--ignore-certificate-errors")
        chrome_options.add_argument("--allow-insecure-localhost")
        chrome_options.add_argument("--ignore-ssl-errors")

        chrome_options.add_argument(
            "user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
            "AppleWebKit/537.36 (KHTML, like Gecko) "
            "Chrome/114.0.0.0 Safari/537.36"
        )

        service = Service(ChromeDriverManager().install())
        driver = webdriver.Chrome(service=service, options=chrome_options)

        driver.get("https://www.sberbank.com/ru/person/credits/money")

        WebDriverWait(driver, 20).until(
            EC.presence_of_element_located(
                (By.CSS_SELECTOR, "div.product-catalog__product-cards")
            )
        )

        offers = []

        cards = driver.find_elements(By.CSS_SELECTOR, "div.product-card")

        for card in cards:

            min_amount = ""
            term = ""
            credit_type = ""

            try:
                credit_type_elem = card.find_element(By.CSS_SELECTOR, "h2.product-card__heading")
                credit_type = credit_type_elem.get_attribute("textContent").strip()

            except Exception as e:
                print(f"Заголовок не найден: {e}")
                credit_type = "Без названия"

            try:
                factoids = card.find_elements(By.CLASS_NAME, "factoid")
                for f in factoids:
                    label = f.find_element(By.CSS_SELECTOR, ".factoid__tooltip .dk-sbol-text").text.lower()
                    value = f.find_element(By.TAG_NAME, "h3").text.strip()
                    if "сумма" in label:
                        min_amount = value
                    if "срок" in label:
                        term = value
            except Exception as e:
                print(f" Ошибка при чтении factoid: {e}")

            if min_amount or term:
                offers.append({
                    "bankName": "Сбербанк",
                    "creditType": credit_type,
                    "minAmount": min_amount,
                    "term": term
                })

        driver.quit()
        return jsonify(offers)

    except Exception:
        tb = traceback.format_exc()
        print(tb)
        return jsonify({"error": tb}), 500


@app.route("/tbank-loan-offers")
def get_tbank_loan_offers():
    try:
        chrome_options = Options()
        chrome_options.add_argument("--headless=new")
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--disable-dev-shm-usage")
        chrome_options.add_argument("--window-size=1920,1080")
        chrome_options.add_argument("--disable-blink-features=AutomationControlled")
        chrome_options.add_argument("user-agent=Mozilla/5.0")

        driver = webdriver.Chrome(
            service=Service(ChromeDriverManager().install()),
            options=chrome_options
        )

        driver.get("https://www.tbank.ru/loans/")
        time.sleep(2)
        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time.sleep(2)


        cards = driver.find_elements(By.CSS_SELECTOR, "div[data-test='panel ']")

        offers = []

        for card in cards[:5]:
            try:
                title = card.find_element(By.CSS_SELECTOR, "[data-test='htmlTag title']").text.strip()
            except:
                title = "Без названия"

            try:
                subtitle = card.find_element(By.CSS_SELECTOR, "[data-test='htmlTag subtitle']").text.strip()
            except:
                subtitle = None

            attrs = []
            try:
                items = card.find_elements(By.CSS_SELECTOR, "ul[data-schema-path='advantages.list'] > li")
                for li in items[:3]:
                    try:
                        attr_title = li.find_element(
                            By.CSS_SELECTOR,
                            "[data-test='htmlTag advantages_list_title']"
                        ).text.strip()
                        attr_sub = li.find_element(
                            By.CSS_SELECTOR,
                            "[data-test='htmlTag advantages_list_subtitle']"
                        ).text.strip()
                        attrs.append({"subtitle": attr_sub, "title": attr_title})
                    except:
                        continue
            except:
                pass

            offers.append({
                "loanName": title,
                "subtitle": subtitle,
                "attributes": attrs
            })

        driver.quit()
        return jsonify(offers)

    except Exception:
        tb = traceback.format_exc()
        print(tb)
        return jsonify({"error": tb}), 500


@app.route("/deposit-offers")
def get_deposit_offers():
    try:
        chrome_options = Options()
        chrome_options.add_argument("--headless=new")
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--disable-dev-shm-usage")
        chrome_options.add_argument("--disable-gpu")
        chrome_options.add_argument("--window-size=1920,1080")
        chrome_options.add_argument("--disable-blink-features=AutomationControlled")
        chrome_options.add_argument("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) "
                                    "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36")

        driver = webdriver.Chrome(service=Service(ChromeDriverManager().install()), options=chrome_options)
        offers = []

        driver.get("https://www.sberbank.com/ru/person/contributions/deposits")


        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time.sleep(2)

        WebDriverWait(driver, 30).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, "h2[data-test-id='DepositCatalog_ProductCard-title']"))
        )

        sber_cards = driver.find_elements(By.CSS_SELECTOR, "[data-test-id='DepositCatalog_ProductCard']")


        for card in sber_cards:
            try:
                name_elem = card.find_element(By.CSS_SELECTOR, "h2[data-test-id='DepositCatalog_ProductCard-title']")
                deposit_name = name_elem.text.strip()
            except Exception:
                deposit_name = "Без названия"

            rate = "Не указано"
            amount = "Не указано"
            term = "Не указано"

            feature_blocks = card.find_elements(By.CSS_SELECTOR, "div.dc-feature")

            for feat in feature_blocks:
                try:
                    label_elem = feat.find_element(By.CLASS_NAME, "dc-feature__text")
                    value_elem = feat.find_element(By.CLASS_NAME, "dk-sbol-heading")
                    label_text = label_elem.text.strip().lower()
                    value_text = value_elem.text.strip()

                    if "ставка" in label_text:
                        rate = value_text
                    elif "сумма" in label_text:
                        amount = value_text
                    elif "срок" in label_text:
                        term = value_text
                except Exception:
                    continue

            offers.append({
                "bankName": "Сбербанк",
                "depositName": deposit_name,
                "rate": rate,
                "amount": amount,
                "term": term
            })

        driver.get("https://www.vtb.ru/personal/vklady-i-scheta/")
        WebDriverWait(driver, 20).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, ".card-mediumstyles__CardTemplate-card-base__sc-senydt-0"))
        )
        time.sleep(2)

        vtb_cards = driver.find_elements(By.CSS_SELECTOR, ".card-mediumstyles__CardTemplate-card-base__sc-senydt-0")

        for card in vtb_cards:
            try:
                name_elem = card.find_element(By.CSS_SELECTOR, "p.typographystyles__Box-foundation-kit__sc-14qzghz-0")
                name = name_elem.text.strip()
            except:
                continue

            label_elems = card.find_elements(By.CSS_SELECTOR,
                                             ".numbersstyles__TypographyDescription-foundation-kit__sc-1xhbrzd-5")
            value_elems = card.find_elements(By.CSS_SELECTOR,
                                             ".numbersstyles__TypographyTitle-foundation-kit__sc-1xhbrzd-4")
            attrs = []
            for i in range(min(len(label_elems), len(value_elems))):
                label = label_elems[i].text.strip()
                value = value_elems[i].text.strip()
                if label and value:
                    attrs.append({"label": label, "value": value})

            if not attrs:
                continue

            offers.append({
                "bankName": "ВТБ",
                "depositName": name,
                "attrs": attrs
            })

        driver.quit()
        return Response(json.dumps(offers, ensure_ascii=False, indent=2),
                        content_type='application/json; charset=utf-8')

    except Exception:
        tb = traceback.format_exc()
        print(tb)
        return jsonify({"error": tb}), 500


@app.route("/tbank-deposit-offers")
def get_tbank_deposit_offers():
    try:
        chrome_options = Options()
        chrome_options.add_argument("--headless=new")
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--disable-dev-shm-usage")
        chrome_options.add_argument("--window-size=1920,1080")
        chrome_options.add_argument("user-agent=Mozilla/5.0")

        service = Service(ChromeDriverManager().install())
        driver = webdriver.Chrome(service=service, options=chrome_options)

        driver.get("https://www.tbank.ru/savings/deposit/")

        driver.execute_script("window.scrollTo(0, document.body.scrollHeight);")
        time.sleep(3)

        WebDriverWait(driver, 20).until(
            EC.presence_of_element_located((By.CSS_SELECTOR, "div.abkW00bPe[data-test='panel ']"))
        )

        panels = driver.find_elements(By.CSS_SELECTOR, "div.abkW00bPe[data-test='panel ']")

        results = []
        for panel in panels:
            try:
                title_elem = panel.find_element(By.CSS_SELECTOR, "div.abagAE--r3.fbagAE--r3.sbagAE--r3[data-test='htmlTag title']")
                subtitle_elem = panel.find_element(By.CSS_SELECTOR, "div.abagAE--r3.lbagAE--r3.sbagAE--r3[data-test='htmlTag subtitle']")
                results.append({
                    "title": title_elem.text.strip(),
                    "subtitle": subtitle_elem.text.strip()
                })
            except:
                continue

        driver.quit()

        results = [item for item in results if len(item["subtitle"]) < 110]

        if len(results) > 3:
            results = results[:-3]

        return jsonify(results)

    except Exception:
        tb = traceback.format_exc()
        print(tb)
        return jsonify({"error": tb}), 500

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
