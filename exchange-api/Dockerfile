FROM python:3

EXPOSE 8080

WORKDIR /usr/src/app

RUN python -m pip install yahoo-historical
RUN pip install googlefinance

RUN git clone https://github.com/lukaszbanasiak/yahoo-finance.git /usr/src/yahoo-finance
RUN cd /usr/src/yahoo-finance && python setup.py install && cd -

RUN pip install git+https://github.com/jsonpickle/jsonpickle.git

RUN pip install lxml
RUN pip install requests

CMD python python_server.py