import sqlite3

con = sqlite3.connect("db.sqlite")
cur = con.cursor()
f = open('hexcodes.txt', 'r')

for line in f:
	t = line[:-1].split(" ")
	cur.execute("INSERT INTO hexcodes VALUES(?, ?);", (t[0], t[1]))

con.commit()
con.close()
