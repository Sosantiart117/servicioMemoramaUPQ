#!/bin/bash

_verify_img(){
    echo "Verify images dir"
    [[ ! -d "target/media" ]] \
        && echo "Faltan las imagenes \"media/img\" y \"media/icons\"" && exit 1 \
        || echo "Image dir Exists"

}

_compile() {
    echo "Compile:"
    [[ ! -d "src" ]] && echo "Wrong path" && exit 1
    javac -d target ./src/com/memorama/* 
    echo "Finsih Compile"
}

_jar() {
    echo "Jar:"
    _compile || exit 1
    _verify_img
    cd ./target
    echo $PWD
    jar cvmf ./manifest.txt ../Memorama.jar com media
}

_run() {
    echo "Run"
    _compile || exit 1
    _verify_img
    cd ./target
    java com.memorama.Main
}

_run_jar() {
    echo "Run Jar"
    [[ -e Memorama.jar ]] && rm -f Memorama.jar
    _jar
    cd ..
    java -jar Memorama.jar
}


_help_message() {
    echo "Usage: $0 [run | jar | compile]"
    echo "  run       : Run the application."
    echo "  runjar    : Run Application in jar"
    echo "  jar       : Create a JAR file."
    echo "  compile   : Compile the code."
    echo "  help      : Show this help message."
}

if [ $# -eq 0 ]; then
    _help_message
    exit 1
fi

case $1 in
    run) _run ;;
    runjar) _run_jar ;;
    jar) _jar ;;
    compile) _compile ;;
    help) _help_message ;;
    *)
        echo "Invalid command: $1"
        _help_message
        exit 1
        ;;
esac

echo "Finish"
