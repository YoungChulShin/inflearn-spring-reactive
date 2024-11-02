#!/bin/bash

for i in {1..30}
do
  BOOK_ID=$(( RANDOM % 100 + 1))

  echo "Send request #$i with book-id: $BOOK_ID"
  curl "http://localhost:8080/v1/books/$BOOK_ID"
  echo
done