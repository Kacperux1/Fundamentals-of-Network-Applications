FROM mongo:8.0.1
COPY ../Docker/keyFile /etc/mongo/keyFile
RUN chown 999:999 /etc/mongo/keyFile && chmod 400 /etc/mongo/keyFile
COPY --chown=999 --chmod=600 ../Docker/mongod.conf /etc/mongod.conf