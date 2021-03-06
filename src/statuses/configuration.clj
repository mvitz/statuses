(ns statuses.configuration)

(def default-config
  {:title              "Status Updates"
   :database-path      "data/db.json"
   :save-interval      1
   :http-port          8080
   :run-mode           :dev
   :profile-url-prefix "https://intern.innoq.com/liqid/users/"
   :avatar-url         "http://assets.github.com/images/gravatars/gravatar-user-420.png"
   :issue-tracker-url  "https://github.com/innoq/statuses/issues"
   :git-commit-url     "https://github.com/innoq/statuses/tree/%s"
   :entry {
           :min-length 1
           :max-length 140}})

(def config-holder (atom default-config))

(defn config
  ([] @config-holder)
  ([key] (get @config-holder key)))

(defn read-git-commit-hash
  "Reads the Git commit hash from headrev.txt.
   This can be generated by calling: git log -1 --pretty=\"%H\" > headrev.txt"
  []
  (clojure.string/trim-newline (slurp "headrev.txt")))

(defn init! [path]
  (if path
    (try
      (println "Initializing configuration from" path ":")
      (swap! config-holder merge (read-string (slurp path)))
      (catch java.io.FileNotFoundException e
        (println "Configuration not found, exiting.")
        (System/exit -1)))
    (println "Using default configuration."))
  (try
    (println "Initializing commit revision from" path ":")
    (swap! config-holder merge {:version (read-git-commit-hash)})
    (println "Version is" (config :version))
    (catch java.io.FileNotFoundException e
      (println "Version not found, continuing anyway"))))

