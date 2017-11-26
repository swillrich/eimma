# elmma-common
The elmma-common provides shared libs which can be used from others.

## Running locally
Simply run:

    node src/app.js

## Running in docker
If you want to run locally you should use docker. Simply bBuild the container:

    docker build -t elmma-common:latest -f devops/Dockerfile .

Then launch it:

    docker run -it -p 3220:3220 elmma-common:latest