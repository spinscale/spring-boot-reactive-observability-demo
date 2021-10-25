from flask import Flask, render_template
import requests
import logging
from elasticapm.contrib.flask import ElasticAPM

app = Flask(__name__)

apm = ElasticAPM(app, logging=logging.INFO,
                 server_url="",
                 service_name="",
                 secret_token="",
                 use_elastic_traceparent_header=True)

@app.route("/")
def home():
    data = requests.get("http://localhost:8080/products/product/123")
    print(data.json())
    return render_template("index.html", data=data.json())

if __name__ == "__main__":
    app.run(debug=False)
