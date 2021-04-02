Pod::Spec.new do |spec|
  spec.name = 'PokedexSDK'
  spec.version = '1.0.0'
  spec.homepage = 'https://www.cocoapods.org'
  spec.source = { :git => "https://github.com/GustavodeJesus/PokedexSDK.git", :tag => "{spec.version}" }
  spec.authors = 'Gustavo de Jesus Gomes'
  spec.license = ''
  spec.summary = 'Project KMM'
  spec.static_framework = true
  spec.vendored_frameworks = "PokedexSDK.framework
  spec.libraries = "c++"
  spec.module_name = "#{spec.name}_umbrella"
  spec.ios.deployment_target = '13.0'
end