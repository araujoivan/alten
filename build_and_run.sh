# Some utilities methods

# Find a text into a file
# e.g  find_string_into_file "hello" text.txt
search_string_into_file() {

	local resp=$(grep -i $1 $2);
	
	[[ -z "$resp" ]] && echo "0" || echo "1"
}

# Find a text into a file
# e.g Schedule a search for the word "hello" into text.txt for 5 times with a interval of 30 seconds
#     scheduled_string_search "hello" text.txt 5 30 "process"
scheduled_string_search() {

    echo "Waiting for $5 to be ready..."

	local attemptCount=0
	local resp=$(search_string_into_file $1 $2)
				
	while [[ "$resp" == 0 ]] && [[ $attemptCount -lt $3 ]]; do
		 
        attemptCount=$((attemptCount + 1));		 
		
		sleep $4
		
		resp=$(search_string_into_file $1 $2)
		
		echo "Verification ${attemptCount} of ${3}..."

	done
	
	if [[ "$resp" == 0 ]]; then echo "Time is over!"; fi
}

generate_random_string() {
	
	local uuid=$(cat /dev/urandom | tr -dc 'a-zA-Z0-9' | fold -w 32 | head -n 1)
	
	echo "$uuid"
}

run_java_app() {

	echo "Starting $1 from apps folder"
	
	local logFileName="$(generate_random_string)_log.out"
	
	nohup java -jar $1 &> $logFileName &
		
	sleep 5
	
    local pattern="Started"	
	
	scheduled_string_search "$pattern" "$logFileName" 8 10 "$1"
			
	echo "$1 started!"
}

build_and_copy_java_app() {

	echo "Bulding $1"

	cd $1

	mvn clean install

	echo "Copying $1 to apps folder"

	cp "target/$1-1.0.0.jar" ../apps
	
	cd ..

}

creating_folders() {

	echo "Removing any previous data created"

	rm -rf db
	rm -rf apps

	echo "Creating a folder where the h2 database file will be store"

	mkdir db

	echo "Creating a folder where api's will be store"

	mkdir apps
}

verify_java() {

	if [[ -z "${JAVA_HOME}" ]]; then
		echo "$JAVA_HOME environmet variable must be set before running this script"
		read -p "Press [Enter]"
		exit 1
	fi
	 
}
 
main() {
	
	verify_java

	creating_folders

	build_and_copy_java_app booking-api

	build_and_copy_java_app listing-api

	cd apps

	run_java_app booking-api-1.0.0.jar

	run_java_app listing-api-1.0.0.jar

	read -p "Press [Enter] to kill java processes and finished the applications."
	
	ps -ef | grep "java"  | grep -v grep | awk -F" " 'system("kill  "$2"")'
	
	read -p "Press [Enter] to close this window."

}

main


