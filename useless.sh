# wf
hell -L 23211:localhost:23211

# payara
ssh -p 2222 s344094@se.ifmo.ru -L 23223:localhost:23223

# casandra reverse
ssh -p 2222 s344094@se.ifmo.ru -R 23432:localhost:23432

# local postgres(yeah)
ssh -p 2222 s344094@se.ifmo.ru -R 23455:localhost:23455