FROM mongo:8.0.1
COPY --chown=999:999 keyFile /etc/mongo/keyFile
RUN chmod 400 /etc/mongo/keyFile
COPY mongod.conf /etc/mongod.conf


