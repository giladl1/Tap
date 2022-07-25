The mission is built with 3 async coroutine calls which allow the functions to run
simultanously . the calls to the website content is asynchronic and that's why the 
launch is in io thread. when the process is finished , we need to publish results to
the ui , and that's why we make another call to main thread . 
2 first functions are straight forward to understand (although the first 1 result is empty "" ).
in the 3rd mission we went through the words we got before , and wrote their occurences in the 
occurences list. instead of checking every time one word , and then to search it all over the words list
we decided to run through the words list one time and check the counts in the other occurences list.
that's why the whole process finished in 2 seconds instead of few minutes.