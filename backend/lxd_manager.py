import pylxd

client = pylxd.Client()

def create_container(name):
    config = {
        'name': name,
        'source': {
            'type': 'copy',
            'source': 'minecraft-template'
        }
    }
    instance = client.instances.create(config, wait=True)
    instance.start(wait=True)

def stop_container(name):
    instance = client.instances.get(name)
    instance.stop(wait=True)

def delete_container(name):
    instance = client.instances.get(name)
    instance.delete(wait=True)
