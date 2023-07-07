# Shamelessly stolen from https://stackoverflow.com/questions/32484504/using-random-to-generate-a-random-string-in-bash
function rand-str {
    # Return random alpha-numeric string of given LENGTH
    #
    # Usage: VALUE=$(rand-str $LENGTH)
    #    or: VALUE=$(rand-str)

    local DEFAULT_LENGTH=64
    local LENGTH=${1:-$DEFAULT_LENGTH}

    LC_ALL=C tr -dc A-Za-z0-9 </dev/urandom | head -c $LENGTH
    # LC_ALL=C: required for Mac OS X - https://unix.stackexchange.com/a/363194/403075
    # -dc: delete complementary set == delete all except given set
}


for i in {1..1}
do
  REASON=$(rand-str)
  ID=$RANDOM
  curl -i --header "Content-Type: application/json" --data "{\"reason\": \"$REASON\"}" http://localhost:7878/fire/$ID
done

