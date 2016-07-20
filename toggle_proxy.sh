if [ -z "$HTTP_PROXY" ]; then
  echo "setting proxy"
  export HTTP_PROXY=http://dt205202:Ep8640mrtc@dstproxy.dstcorp.net:9119
  export http_proxy=http://dt205202:Ep8640mrtc@dstproxy.dstcorp.net:9119
  export HTTPS_PROXY=http://dt205202:Ep8640mrtc@dstproxy.dstcorp.net:9119
  export https_proxy=http://dt205202:Ep8640mrtc@dstproxy.dstcorp.net:9119
else
  echo "unsetting proxy"
  unset HTTP_PROXY
  unset http_proxy
  unset HTTPS_PROXY
  unset https_proxy
fi