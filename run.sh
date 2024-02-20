#!/bin/bash
# Edit this

_out_dir="dist"
_jar_path="$_out_dir/Memorama.jar"
_media_path="$_out_dir/media"

_jar() {
    echo "Jar:"
    _compile || exit 1
    _verify_img
    cd ./$_out_dir
    echo $PWD
    [[ -e $_jar_path ]] \
        && jar uvmf manifest.txt Memorama.jar com media \
        || jar cvmf manifest.txt Memorama.jar com media

    cd ..
}

_update_modules(){
    _jar
    echo "Updating Modules list"
    jdeps --list-reduced-deps $_jar_path | tr -d ' ' > $_out_dir/modules.txt
}

_create_jre() {
    _update_modules
    echo "Creating JRE"
    output_directory="$_out_dir/JRE"
    modules=$(cat $_out_dir/modules.txt | tr '\n' ',')
    [[ -d $output_directory ]] && rm -rf $output_directory
    jlink \
        --module-path $(dirname $(dirname $(readlink -f $(which java))))/jmods \
        --add-modules "$modules" \
        --output "$output_directory"
}

_verify_img(){
    echo "Verify images dir"
    [[ ! -d $_media_path ]] \
        && echo "Faltan las imagenes \"media/img\" y \"media/icons\"" && exit 1 \
        || echo "Image dir Exists"

}

_compile() {
    echo "Compile:"
    [[ ! -d "src" ]] && echo "Wrong path" && exit 1
    javac -d $_out_dir ./src/com/memorama/* 
    echo "Finsih Compile"
}


_exe(){
    [[ -d "$_out_dir/JRE" ]] || _create_jre
    [[ ! -e "$_jar_path" ]] && _jar
    # launch4jc $_out_dir/config.xml
}

_zip(){
    _exe
    zip -r $_out_dir/Memorama.zip $_out_dir/Memorama.exe $_out_dir/JRE
}

_run() {
    echo "Run"
    _compile || exit 1
    _verify_img
    cd ./$_out_dir
    java com.memorama.Main
}

_run_jar() {

    echo "Run Jar"
    [[ -e $_jar_path ]] && rm -f $_jar_path
    _jar
    java -jar $_jar_path
}


_help_message() {
    echo "Usage: $0 [run | jar | compile]"
    echo "  run       : Run the application."
    echo "  runjar    : Run Application in jar."
    echo "  jar       : Create a JAR file."
    echo "  compile   : Compile the code."
    echo "  jre       : Create JRE for the app."
    echo "  exe       : Create exe (uses launch4j)"
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
    jre) _create_jre ;;
    exe) _exe ;;
    zip) _zip ;;
    help) _help_message ;;
    *)
        echo "Invalid command: $1"
        _help_message
        exit 1
        ;;
esac

echo "Finish"
